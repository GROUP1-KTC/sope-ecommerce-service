package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.PaymentRequest;
import com.sope.sope_ecommerce_backend.dto.response.PaymentResponse;
import com.sope.sope_ecommerce_backend.entities.Order;
import com.sope.sope_ecommerce_backend.entities.Payment;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.enums.PaymentProvider;
import com.sope.sope_ecommerce_backend.enums.PaymentStatus;
import com.sope.sope_ecommerce_backend.exception.CustomException;
import com.sope.sope_ecommerce_backend.mapper.PaymentMapper;
import com.sope.sope_ecommerce_backend.repositories.OrderRepository;
import com.sope.sope_ecommerce_backend.repositories.PaymentRepository;
import com.sope.sope_ecommerce_backend.services.OrderService;
import com.sope.sope_ecommerce_backend.services.PaymentService;
import com.sope.sope_ecommerce_backend.services.gateways.PaymentGateway;
import com.sope.sope_ecommerce_backend.services.gateways.PaymentGatewayFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Override
    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest request) {
        Optional<Payment> existing = paymentRepository.findByIdempotencyKey(request.idempotencyKey());
        if (existing.isPresent()) {
            return paymentMapper.toPaymentResponse(existing.get());
        }

        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new CustomException("Order not found"));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new CustomException("Order not in pending status");
        }
        if (order.getPayment() != null) {
            throw new CustomException("Payment already initiated");
        }

        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID())
                .order(order)
                .amount(order.getTotalAmount())
                .paymentMethod(request.method())
                .status(PaymentStatus.PENDING)
                .provider(request.provider().name())
                .idempotencyKey(request.idempotencyKey())
                .build();

        PaymentGateway gateway = gatewayFactory.getGateway(request.provider());
        String[] gatewayResponse = gateway.createPaymentIntent(order.getTotalAmount(), request.idempotencyKey());
        payment.setProviderPayUrl(gatewayResponse[0]);
        payment.setProviderPaymentId(gatewayResponse[1]);

        payment = paymentRepository.save(payment);

        order.setPayment(payment);
        orderRepository.save(order);

        return paymentMapper.toPaymentResponse(payment);
    }

    @Override
    @Transactional
    public void handlePaymentCallback(String providerStr, String providerPaymentId, String callbackStatus, String otherParamsJsonOrQuery) {
        // 1) tìm payment
//        Payment payment = paymentRepository.findByProviderPaymentId(providerPaymentId)
//                .orElseThrow(() -> new CustomException("Payment not found"));
//        if (payment.getStatus() != PaymentStatus.PENDING) return; // idempotent
//
//        // 2) parse params → Map<String,String>
//        Map<String, String> params = CallbackParser.parse(otherParamsJsonOrQuery); // tự cài: nếu query -> parse querystring; nếu JSON -> parse Jackson
//
//        // 3) verify chữ ký bởi từng gateway
//        PaymentProvider provider = PaymentProvider.valueOf(providerStr);
//        PaymentGateway gw = gatewayFactory.getGateway(provider);
//        if (!gw.verifyCallback(params)) throw new CustomException("Invalid callback signature");
//
//        // 4) map status provider → domain status
//        PaymentStatus newStatus = gw.mapStatus(callbackStatus);
//        payment.setStatus(newStatus);
//
//        // 5) cập nhật đơn
//        if (newStatus == PaymentStatus.SUCCESS) {
//            payment.setPaymentTime(LocalDateTime.now());
//            orderService.updateOrderStatus(new UpdateOrderStatusRequest(payment.getOrder().getOrderId(), OrderStatus.CONFIRMED));
//        } else {
//            orderService.updateOrderStatus(new UpdateOrderStatusRequest(payment.getOrder().getOrderId(), OrderStatus.PAYMENT_FAILED));
//        }
//        paymentRepository.save(payment);
    }

}
