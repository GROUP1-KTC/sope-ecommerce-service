package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.OrderCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.OrderResponse;
import com.sope.sope_ecommerce_backend.entities.*;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.exception.CustomException;
import com.sope.sope_ecommerce_backend.repositories.OrderRepository;
import com.sope.sope_ecommerce_backend.repositories.OrderStatusHistoryRepository;
import com.sope.sope_ecommerce_backend.repositories.ProductVariantRepository;
import com.sope.sope_ecommerce_backend.services.AddressService;
import com.sope.sope_ecommerce_backend.services.CartService;
import com.sope.sope_ecommerce_backend.services.OrderService;
import com.sope.sope_ecommerce_backend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository statusHistoryRepository;
    private final CartService cartService;
    private final ProductVariantRepository productVariantService;
//    private final DiscountCodeService discountCodeService;
    private final UserService userService;
    private final AddressService addressService;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
//        // Get entities from IDs
//        User user = userService.getUserById(request.userId());
//        Address shippingAddress = addressService.getAddressById(request.shippingAddressId());
//
//        // Check idempotency
//        Optional<Order> existingOrder = orderRepository.findByIdempotencyKey(request.idempotencyKey());
//        if (existingOrder.isPresent()) {
//            return dtoMapper.toOrderResponse(existingOrder.get());
//        }
//
//        Cart cart = cartService.getCartByUser(user);
//        if (cart.getItems().isEmpty()) {
//            throw new CustomException("Cart is empty");
//        }
//
//        // Calculate subtotal and orderItems (giống trước)
//        BigDecimal subtotal = BigDecimal.ZERO;
//        List<OrderItem> orderItems = new ArrayList<>();
//        for (CartItem cartItem : cart.getItems()) {
//            ProductVariantEntity variant = cartItem.getProductVariant();
//            if (variant.getStock() < cartItem.getQuantity()) {
//                throw new CustomException("Insufficient stock for " + variant.getProduct().getName());
//            }
//            BigDecimal itemPrice = variant.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
//            subtotal = subtotal.add(itemPrice);
//
//            OrderItem orderItem = OrderItem.builder()
//                    .orderItemId(OrderItemId.builder()
//                            .orderId(null)
//                            .productVariantId(variant.getId())
//                            .build())
//                    .productVariant(variant)
//                    .quantity(cartItem.getQuantity())
//                    .price(variant.getPrice())
//                    .build();
//            orderItems.add(orderItem);
//
//            variant.setStock(variant.getStock() - cartItem.getQuantity());
//            productVariantService.save(variant);
//        }
//
//        // Apply discount
//        BigDecimal discountAmount = BigDecimal.ZERO;
//        DiscountCodeEntity discount = null;
//        if (request.discountCodeId() != null) {
//            discount = discountCodeService.getById(UUID.fromString(request.discountCodeId()));
//            if (discount.isValid()) {
//                discountAmount = discount.calculateDiscount(subtotal);
//            } else {
//                throw new CustomException("Invalid discount code");
//            }
//        }
//
//        BigDecimal shippingCharges = BigDecimal.valueOf(10.00); // Mock
//        BigDecimal totalAmount = subtotal.subtract(discountAmount).add(shippingCharges);
//
//        Order order = Order.builder()
//                .orderId(UUID.randomUUID())
//                .user(user)
//                .shippingAddress(shippingAddress)
//                .orderDate(LocalDateTime.now())
//                .subtotal(subtotal)
//                .shippingCharges(shippingCharges)
//                .totalAmount(totalAmount)
//                .note(request.note())
//                .status(OrderStatus.PENDING)
//                .orderItems(orderItems)
//                .idempotencyKey(request.idempotencyKey())
//                .discountCode(discount)
//                .statusHistory(new ArrayList<>())
//                .build();
//
//        orderItems.forEach(item -> {
//            item.setOrder(order);
//            item.getOrderItemId().setOrderId(order.getOrderId());
//        });
//
//        addStatusHistory(order, OrderStatus.PENDING);
//
//        order = orderRepository.save(order);
//
//        cartService.clearCart(cart);
//
//        return dtoMapper.toOrderResponse(order);
        return null;
    }

    @Override
    public Iterable<OrderResponse> getAllOrders() {
        return null;
    }

    @Override
    public Iterable<OrderResponse> getOrdersByUserId(UUID userId) {
        return null;
    }

    @Override
    public OrderResponse getOrderById(UUID id) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new CustomException("Order not found"));
//        return dtoMapper.toOrderResponse(order);
        return null;
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(UUID id) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new CustomException("Order not found"));
//        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.PROCESSING) {
//            throw new CustomException("Cannot cancel order in current status");
//        }
//
//        // Refund if needed...
//
//        for (OrderItem item : order.getOrderItems()) {
//            ProductVariantEntity variant = item.getProductVariant();
//            variant.setStock(variant.getStock() + item.getQuantity());
//            productVariantService.save(variant);
//        }
//
//        order.setStatus(OrderStatus.CANCELLED);
//        addStatusHistory(order, OrderStatus.CANCELLED);
//        order = orderRepository.save(order);
//
//        return dtoMapper.toOrderResponse(order);
        return null;
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(UUID id, OrderCreateRequest order) {
        return null;
    }


    private void addStatusHistory(Order order, OrderStatus status) {
        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
        order.getStatusHistory().add(history);
    }
}
