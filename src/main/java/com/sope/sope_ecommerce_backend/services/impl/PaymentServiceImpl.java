package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.*;
import com.sope.sope_ecommerce_backend.dto.response.PaymentResponse;
import com.sope.sope_ecommerce_backend.entities.*;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.enums.PaymentProvider;
import com.sope.sope_ecommerce_backend.enums.PaymentStatus;
import com.sope.sope_ecommerce_backend.events.PaymentCreatedEvent;
import com.sope.sope_ecommerce_backend.exception.CustomException;
import com.sope.sope_ecommerce_backend.mapper.OrderMapper;
import com.sope.sope_ecommerce_backend.mapper.PaymentMapper;
import com.sope.sope_ecommerce_backend.repositories.PaymentRepository;
import com.sope.sope_ecommerce_backend.repositories.TempOrderRepository;
import com.sope.sope_ecommerce_backend.services.*;
import com.sope.sope_ecommerce_backend.services.gateways.PaymentGateway;
import com.sope.sope_ecommerce_backend.services.gateways.PaymentGatewayFactory;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final PaymentGatewayFactory gatewayFactory;
    private final PaymentMapper paymentMapper;
    private final TempOrderRepository tempOrderRepository;
    private final ProductVariantService productVariantService;
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final AddressService addressService;
    private final EmailService emailService;
    private final ApplicationEventPublisher eventPublisher;

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
        Payment payment = paymentRepository.findById(request.paymentId())
                .orElseThrow(() -> new CustomException("Payment not found"));



        PaymentGateway<? extends PaymentResponse> gateway = gatewayFactory.getGateway(request.provider());
        PaymentResponse gatewayResponse = gateway.createPaymentIntent(request);

           payment.setProviderPaymentId(gatewayResponse.getPaymentId());
           payment.setProviderPayUrl(gatewayResponse.getPayUrl());
           payment.setRequestId(request.requestId());

        Payment newpayment = paymentRepository.save(payment);

        System.out.println(newpayment);


        List<TempOrder> tempOrders = payment.getTempOrders();


        List<OrderEmailRequest> ordersForEmail = tempOrders.stream()
                .map(o -> {
                    List<OrderItemEmailRequest> items = o.getOrderItems().stream()
                            .map(i -> new OrderItemEmailRequest(
                                    i.getProductVariant() != null && i.getProductVariant().getProduct() != null
                                            ? i.getProductVariant().getProduct().getName()
                                            : "Unknown",
                                    i.getQuantity(),
                                    i.getPrice()
                            ))
                            .toList();


                    return OrderEmailRequest.builder()
                            .code(o.getOrderNumber())
                            .date(o.getCreatedAt())
                            .status(o.getPaymentStatus().name())
                            .items(items)
                            .totalAmount(o.getTotalAmount())
                            .guestName(o.getGuestName())
                            .guestEmail(o.getGuestEmail())
                            .guestPhone(o.getGuestPhone())
                            .guestAddress(o.getShippingAddress() + ", " + o.getWard() + ", " + o.getDistrict() + ", " + o.getCity())
                            .build();
                })
                .toList();

        System.out.println("Publishing PaymentCreatedEvent for " + ordersForEmail.size() + " orders." + ordersForEmail);




        eventPublisher.publishEvent(new PaymentCreatedEvent(ordersForEmail, gatewayResponse));

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
                OrderStatus status = newStatus == PaymentStatus.SUCCESS ? OrderStatus.CONFIRMED : OrderStatus.CANCELLED;
                orderService.updateOrderStatus(UpdateOrderStatusRequest.builder()
                        .orderId(order.getOrderId())
                        .status(status)
                        .build());
            }
        }
        else if (payment.getTempOrders() != null && !payment.getTempOrders().isEmpty()) {
            List<TempOrder> tempOrdersToRemove = new ArrayList<>();

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
                                    .isDefault(true)
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
                    payment.addOrder(order);

                    tempOrdersToRemove.add(tempOrder);
                    orderService.saveOrder(order);
                } else {
                    tempOrder.setPaymentStatus(PaymentStatus.FAILED);
                    tempOrder.setExpiresAt(null);

                    tempOrder.getOrderItems().forEach(item ->
                            productVariantService.retrieveProductVariantStock(item.getProductVariant().getProductVariantId(), item.getQuantity())
                    );
                    tempOrder.getOrderItems().clear();
                    tempOrderRepository.save(tempOrder);
                }
            }
            payment.getTempOrders().removeAll(tempOrdersToRemove);
        }

        paymentRepository.save(payment);
    }
}
