package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.BasePaymentDTO;
import com.sope.sope_ecommerce_backend.dto.request.PaymentRequest;
import com.sope.sope_ecommerce_backend.dto.request.UpdateOrderStatusRequest;
import com.sope.sope_ecommerce_backend.dto.response.PaymentResponse;
import com.sope.sope_ecommerce_backend.entities.Order;
import com.sope.sope_ecommerce_backend.entities.OrderItem;
import com.sope.sope_ecommerce_backend.entities.Payment;
import com.sope.sope_ecommerce_backend.entities.TempOrder;
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
import com.sope.sope_ecommerce_backend.services.OrderService;
import com.sope.sope_ecommerce_backend.services.PaymentService;
import com.sope.sope_ecommerce_backend.services.ProductVariantService;
import com.sope.sope_ecommerce_backend.services.UserService;
import com.sope.sope_ecommerce_backend.services.gateways.PaymentGateway;
import com.sope.sope_ecommerce_backend.services.gateways.PaymentGatewayFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Override
    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest request, UUID userId) {
        Optional<Payment> existing = paymentRepository.findByIdempotencyKey(request.idempotencyKey());
        if (existing.isPresent()) {
            BasePaymentDTO dto = paymentMapper.toBasePaymentDTO(existing.get());
//            return paymentResponseFactory.from(dto, request.provider());
            return null;
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
             tempOrder = tempOrderRepository.findByIdempotencyKey(request.tempOrderCode())
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

       payment.setProvider(request.provider().name());
       payment.setProviderPaymentId(gatewayResponse.getPaymentId());
       payment.setProviderPayUrl(gatewayResponse.getPayUrl());


        paymentRepository.save(payment);
        return gatewayResponse;
    }

    @Override
    @Transactional
    public void handlePaymentCallback(String providerStr, String providerPaymentId, String callbackStatus, String otherParamsJsonOrQuery) {
        // 1️⃣ tìm payment
        Payment payment = paymentRepository.findByProviderPaymentId(providerPaymentId)
                .orElseThrow(() -> new CustomException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.PENDING) return;

        // 2️⃣ parse params → Map<String,String>
//        Map<String, String> params = CallbackParser.parse(otherParamsJsonOrQuery);

        // 3️⃣ verify chữ ký bởi gateway
        PaymentProvider provider = PaymentProvider.valueOf(providerStr);
        PaymentGateway gw = gatewayFactory.getGateway(provider);
//        if (!gw.verifyCallback(params)) throw new CustomException("Invalid callback signature");

        // 4️⃣ map status provider → domain status
        PaymentStatus newStatus = gw.mapStatus(callbackStatus);
        payment.setStatus(newStatus);

        // 5️⃣ xử lý theo loại đơn
        if (payment.getOrder() != null) {
            if (newStatus == PaymentStatus.SUCCESS) {
                payment.setPaymentTime(LocalDateTime.now());
                orderService.updateOrderStatus(new UpdateOrderStatusRequest(payment.getOrder().getOrderId(), OrderStatus.CONFIRMED));
            } else {
                orderService.cancelOrder(payment.getOrder().getOrderId());
            }
        } else if (payment.getTempOrder() != null) {
            TempOrder tempOrder = payment.getTempOrder();

            List<OrderItem> orderItems = orderMapper.tempOrderToOrderItemsEntity(tempOrder.getOrderItems());

            if (newStatus == PaymentStatus.SUCCESS) {
                Order order = Order.builder()
                        .appUser(userService.getOrCreateGuestUser(tempOrder.getGuestEmail(), tempOrder.getGuestName(), tempOrder.getGuestPhone()))
                        .orderItems(orderItems)
                        .subtotal(tempOrder.getSubtotal())
                        .shippingCharges(tempOrder.getShippingCharges())
                        .totalAmount(tempOrder.getTotalAmount())
                        .status(OrderStatus.CONFIRMED)
                        .orderDate(LocalDateTime.now())
                        .orderNumber(tempOrder.getOrderNumber())
                        .build();

                order.setPayment(payment);
                payment.setOrder(order);
                payment.setTempOrder(null);

                orderRepository.save(order);

                // Xóa TempOrder
                tempOrderRepository.delete(tempOrder);

            } else {
                // Payment failed → rollback stock, mark tempOrder
                tempOrder.setPaymentStatus(PaymentStatus.FAILED);
                tempOrderRepository.save(tempOrder);
                tempOrder.getOrderItems().forEach(item ->
                        productVariantService.retrieveProductVariantStock(item.getProductVariantId(), item.getQuantity())
                );
            }
        }

        paymentRepository.save(payment);
    }
}
