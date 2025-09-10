package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.GuestOrderCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.OrderCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.UpdateOrderStatusRequest;
import com.sope.sope_ecommerce_backend.dto.response.OrderResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserOrderResponse;
import com.sope.sope_ecommerce_backend.entities.*;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.exception.CustomException;
import com.sope.sope_ecommerce_backend.mapper.OrderMapper;
import com.sope.sope_ecommerce_backend.repositories.OrderRepository;
import com.sope.sope_ecommerce_backend.repositories.OrderStatusHistoryRepository;
import com.sope.sope_ecommerce_backend.repositories.TempOrderRepository;
import com.sope.sope_ecommerce_backend.services.*;
import com.sope.sope_ecommerce_backend.services.patterns.OrderCreationStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository statusHistoryRepository;
    private final CartService cartService;
    private final UserService userService;
    private final AddressService addressService;
    private final OrderMapper orderMapper;
    private final ProductVariantService productVariantService;
    private final TempOrderRepository tempOrderRepository;

    private final List<OrderCreationStrategy<?>> strategies;


    /**
     * Creates a new order based on the provided request.
     *
     * @param request the order creation request containing user ID, shipping address ID, and other details
     * @return the created order response
     */
    @Override
    @Transactional
    public List<? extends OrderResponse> createOrder(OrderCreateRequest request, UUID userId) {

        String idempotencyKey = request.idempotencyKey();

        List<Order> matchingOrders = orderRepository.findAllByIdempotencyKeyContaining(idempotencyKey);

        if (!matchingOrders.isEmpty()) {
            return matchingOrders.stream()
                    .map(orderMapper::toOrderResponseDTO)
                    .toList();
        }

        List<TempOrder> matchingTempOrders = tempOrderRepository.findAllByIdempotencyKeyContaining(idempotencyKey);

        if (!matchingTempOrders.isEmpty()) {
            return matchingTempOrders.stream()
                    .map(orderMapper::toOrderResponseDTO)
                    .toList();
        }

        OrderCreationStrategy strategy = strategies.stream()
                .filter(s -> s.supports(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported order request type"));


        return strategy.createOrder(request, userId);
    }

    @Override
    @Transactional
    public void saveOrder(Order order) {
        addStatusHistory(order, order.getStatus());
        orderRepository.save(order);
    }

    @Override
    public List<? extends OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new CustomException("No orders found");
        }

        return orderMapper.toUserOrderResponseDTOs(orders);
    }

    @Override
    public List<? extends OrderResponse> getOrdersByUserId(UUID userId) {

        AppUser appUser = userService.getUserEntityById(userId);

        List<Order> orders = orderRepository.findByAppUser(appUser);
        if (orders.isEmpty()) {
            throw new CustomException("No orders found for user");
        }


        return orderMapper.toUserOrderResponseDTOs(orders);
    }

    @Override
    public OrderResponse getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found"));
        return orderMapper.toOrderResponseDTO(order);
    }


    @Override
    public  List<Order> findOrderByStatusPendingAndExpireAtBefore(LocalDateTime now){
        List<Order> orders = orderRepository.findByStatusAndExpireAtBefore(OrderStatus.PENDING, now);
        if (orders.isEmpty()) {
            throw new CustomException("No pending orders found that are expired");
        }
        return orders;
    }

    @Override
    @Transactional
    public void cancelOrder(UUID id, String reason, UUID userId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new CustomException("Order not found"));

        if (!order.getAppUser().getId().equals(userId)) {
            throw new CustomException("You are not authorized to cancel this order");
        }

        OrderStatus newStatus = OrderStatus.CANCELLED;

        if (order.getStatus() == newStatus) {
            return;
        }

        if (!isValidStatusTransition(order.getStatus(), newStatus)) {
            throw new CustomException("Invalid status transition from " + order.getStatus() + " to " + newStatus);
        }

        List<OrderItem> items = order.getOrderItems();
        for (OrderItem item : items) {
            ProductVariant productVariant = item.getProductVariant();
            productVariant.setStock(productVariant.getStock() + item.getQuantity());
            productVariant.setSold(productVariant.getSold() - item.getQuantity());
            productVariantService.saveProductVariant(productVariant);
        }

        order.setStatus(newStatus);
        order.setCancelReason(reason);
        addStatusHistory(order, newStatus);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(UUID id, OrderCreateRequest order) {
        return null;
    }




    @Transactional
    public void updateOrderStatus(UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new CustomException("Order not found"));

        OrderStatus newStatus = request.status();

        if (order.getStatus() == newStatus) {
            return;
        }

        if (!isValidStatusTransition(order.getStatus(), newStatus)) {
            throw new CustomException("Invalid status transition from " + order.getStatus() + " to " + newStatus);
        }

        order.setExpireAt(null);
        order.setStatus(newStatus);
        addStatusHistory(order, newStatus);
        orderRepository.save(order);
    }



    /**
     * Adds a status history entry for the given order.
     *
     * @param order  the order to which the status history will be added
     * @param status the new status to be recorded
     */
    private void addStatusHistory(Order order, OrderStatus status) {
        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
        order.getStatusHistory().add(history);
    }

    private boolean isValidStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // Define valid transitions here
        switch (currentStatus) {
            case PENDING:
                return newStatus == OrderStatus.CONFIRMED || newStatus == OrderStatus.CANCELLED;
            case CONFIRMED:
                return newStatus == OrderStatus.SHIPPING || newStatus == OrderStatus.CANCELLED;
            case SHIPPING:
                return newStatus == OrderStatus.DELIVERED;
            case DELIVERED:
                return newStatus == OrderStatus.REFUNDED;
            case CANCELLED:
                return false;
            default:
                throw new CustomException("Unknown order status: " + currentStatus);
        }
    }
}
