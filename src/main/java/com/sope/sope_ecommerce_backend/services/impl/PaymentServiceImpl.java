package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.BasePaymentDTO;
import com.sope.sope_ecommerce_backend.dto.request.AddressCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.PaymentRequest;
import com.sope.sope_ecommerce_backend.dto.request.UpdateOrderStatusRequest;
import com.sope.sope_ecommerce_backend.dto.response.PaymentResponse;
import com.sope.sope_ecommerce_backend.entities.*;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.enums.PaymentMethod;
import com.sope.sope_ecommerce_backend.enums.PaymentProvider;
import com.sope.sope_ecommerce_backend.enums.PaymentStatus;
import com.sope.sope_ecommerce_backend.exception.CustomException;
import com.sope.sope_ecommerce_backend.mapper.OrderMapper;
import com.sope.sope_ecommerce_backend.mapper.PaymentMapper;
import com.sope.sope_ecommerce_backend.repositories.OrderRepository;
import com.sope.sope_ecommerce_backend.repositories.PaymentRepository;
import com.sope.sope_ecommerce_backend.repositories.TempOrderRepository;
import com.sope.sope_ecommerce_backend.services.*;
import com.sope.sope_ecommerce_backend.services.gateways.PaymentGateway;
import com.sope.sope_ecommerce_backend.services.gateways.PaymentGatewayFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Provider;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final PaymentGatewayFactory gatewayFactory;
    private final PaymentMapper paymentMapper;
    private  final OrderRepository orderRepository;
    private final TempOrderRepository tempOrderRepository;
    private final ProductVariantService productVariantService;
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final AddressService addressService;

    @Override
    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest request, UUID userId) {
        Optional<Payment> existing = paymentRepository.findByRequestId(request.requestId());
        if (existing.isPresent()) {
            Payment existingPayment = existing.get();
            if (existingPayment.getStatus() == PaymentStatus.PENDING) {
                return paymentMapper.toBasePaymentDTO(existingPayment);
            } else {
                throw new CustomException("Payment already processed with status: " + existingPayment.getStatus());
            }
        }

        TempOrder tempOrder = null;
        Order order = null;

        if (userId != null) {
             order = orderRepository.findById(request.orderId())
                    .orElseThrow(() -> new CustomException("Order not found"));

            if (order.getStatus() != OrderStatus.PENDING && order.getPayment().getStatus() != PaymentStatus.PENDING) {
                throw new CustomException("Order not in pending status");
            }

        } else if (!request.tempOrderCode().isEmpty()) {
             tempOrder = tempOrderRepository.findById(UUID.fromString(request.tempOrderCode()))
                    .orElseThrow(() -> new CustomException("TempOrder not found or expired"));

            if (tempOrder.getPaymentStatus() != PaymentStatus.PENDING) {
                throw new CustomException("Payment already initiated or expired");
            }

        } else {
            throw new IllegalArgumentException("Either userId or tempOrderCode must be provided");
        }

        PaymentGateway<? extends PaymentResponse> gateway = gatewayFactory.getGateway(request.provider());
        PaymentResponse gatewayResponse = gateway.createPaymentIntent(request);

        Payment payment = order != null ? order.getPayment() : tempOrder.getPayment();

           payment.setProviderPaymentId(gatewayResponse.getPaymentId());
           payment.setProviderPayUrl(gatewayResponse.getPayUrl());
           payment.setRequestId(request.requestId());


        paymentRepository.save(payment);
        return gatewayResponse;
    }

    @Override
    @Transactional
    public void handlePaymentCallback(String requestId, String callbackStatus, PaymentProvider provider) {
        Payment payment = paymentRepository.findByRequestId(requestId)
                .orElseThrow(() -> new CustomException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.PENDING) return;


        PaymentGateway gw = gatewayFactory.getGateway(provider);

        PaymentStatus newStatus = gw.mapStatus(callbackStatus);
        payment.setStatus(newStatus);

        if (payment.getOrders() != null && !payment.getOrders().isEmpty()) {
            for (Order order : payment.getOrders()) {
                if (newStatus == PaymentStatus.SUCCESS) {
                    orderService.updateOrderStatus(
                            UpdateOrderStatusRequest.builder()
                                    .orderId(order.getOrderId())
                                    .status(OrderStatus.CONFIRMED)
                                    .build()
                    );
                } else {
                    orderService.updateOrderStatus(
                            UpdateOrderStatusRequest.builder()
                                    .orderId(order.getOrderId())
                                    .status(OrderStatus.CANCELLED)
                                    .build()
                    );
                }
            }
        } else if (payment.getTempOrders() != null && !payment.getTempOrders().isEmpty()) {
            for (TempOrder tempOrder : payment.getTempOrders()) {
                List<OrderItem> orderItems = orderMapper.tempOrderToOrderItemsEntity(tempOrder.getOrderItems());


                if (newStatus == PaymentStatus.SUCCESS) {
                    AppUser user = userService.getOrCreateGuestUser(tempOrder.getGuestEmail(), tempOrder.getGuestName(), tempOrder.getGuestPhone());


                    Address address = addressService.getOrCreateAddress(
                            user.getId(),
                            AddressCreateRequest.builder()
                                    .country("Vietnam")
                                    .city(tempOrder.getCity())
                                    .district(tempOrder.getDistrict())
                                    .ward(tempOrder.getWard())
                                    .phoneNumber(tempOrder.getGuestPhone())
                                    .recipientName(tempOrder.getGuestName())
                                    .street(tempOrder.getShippingAddress())
                                    .isDefault(false)
                                    .build());


                    Order order = Order.builder()
                            .appUser(user)
                            .shop(tempOrder.getShop())
                            .subTotal(tempOrder.getSubTotal())
                            .shippingCharges(tempOrder.getShippingCharges())
                            .totalAmount(tempOrder.getTotalAmount())
                            .status(OrderStatus.CONFIRMED)
                            .orderDate(LocalDateTime.now())
                            .orderNumber(tempOrder.getOrderNumber())
                            .shippingRateId(tempOrder.getShippingRateId())
                            .idempotencyKey(tempOrder.getIdempotencyKey())
                            .shippingAddress(address)
                            .build();


                    orderItems.forEach(item ->{
                        OrderItemId orderItemId = OrderItemId.builder()
                                .orderId(order.getOrderId())
                                .productVariantId(item.getProductVariant().getProductVariantId()
                                ).build();

                        item.setOrder(order);
                        item.setOrderItemId(orderItemId);
                    });

                    order.setOrderItems(orderItems);
                    order.setPayment(payment);

                    payment.addOrder(order);
                    payment.setTempOrders(null);

                    orderRepository.save(order);
                    tempOrder.setPaymentStatus(PaymentStatus.SUCCESS);
                    tempOrderRepository.save(tempOrder);

                } else {
                    tempOrder.setPaymentStatus(PaymentStatus.FAILED);
                    tempOrder.setExpiresAt(null);
                    tempOrderRepository.save(tempOrder);

                    tempOrder.getOrderItems().forEach(item ->
                            productVariantService.retrieveProductVariantStock(item.getProductVariant().getProductVariantId(), item.getQuantity())
                    );

                }
            }
        }

        paymentRepository.save(payment);
    }
}
