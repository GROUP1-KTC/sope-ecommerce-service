package com.sope.sope_ecommerce_backend.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sope.sope_ecommerce_backend.dto.request.GuestOrderCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.OrderCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.OrderItemRequest;
import com.sope.sope_ecommerce_backend.dto.response.OrderResponse;
import com.sope.sope_ecommerce_backend.entities.*;

import com.sope.sope_ecommerce_backend.enums.DiscountScope;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.exception.CustomException;
import com.sope.sope_ecommerce_backend.mapper.OrderMapper;
import com.sope.sope_ecommerce_backend.repositories.TempOrderRepository;
import com.sope.sope_ecommerce_backend.services.AddressService;
import com.sope.sope_ecommerce_backend.services.ProductVariantService;
import com.sope.sope_ecommerce_backend.services.UserService;
import com.sope.sope_ecommerce_backend.services.patterns.OrderCreationStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.sope.sope_ecommerce_backend.utils.RandomUtil.generateKey;

@Service
@AllArgsConstructor
public class GuestOrderServiceImpl implements OrderCreationStrategy<GuestOrderCreateRequest> {
    private final UserService userService;
    private final ProductVariantService productVariantService;
    private final TempOrderRepository tempOrderRepository;
    private final OrderMapper orderMapper;

    @Override
    public boolean supports(OrderCreateRequest request) {
        return request instanceof GuestOrderCreateRequest;
    }

    @Override
    public OrderResponse createOrder(GuestOrderCreateRequest request, UUID userId) {
        if (request.guestInfo().fullName() == null || request.guestInfo().phone() == null) {
            throw new IllegalArgumentException("Guest info incomplete");
        }

        if (request.paymentMethod().equals("COD")) {
            throw new IllegalArgumentException("Guest cannot pay by COD");
        }

        AppUser user = userService.getUserEntityByEmail(request.guestInfo().email());

        Address shippingAddress = null;


//        if(user == null){
//            // Tạo Guest mới
//            user = userService.createGuestUser(request.guestInfo());
//        } else if(user.getRole() == Role.CUSTOMER) {
//            // Có thể throw exception hoặc cho phép checkout như customer
//        } else if(user.getRole() == Role.GUEST) {
//            // Guest hiện tại → dùng luôn
//        }


        // Calculate subtotal and orderItems
        BigDecimal subtotal = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : request.items()) {
            ProductVariant variant = productVariantService.getProductVariantEntityById(orderItemRequest.productVariantId()) ;
            if (variant.getStock() < orderItemRequest.quantity()) {
                throw new CustomException("Insufficient stock for " + variant.getProduct().getName());
            }
            BigDecimal itemPrice = variant.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.quantity()));
            subtotal = subtotal.add(itemPrice);

            OrderItem orderItem = OrderItem.builder()
                    .orderItemId(OrderItemId.builder()
                            .orderId(null)
                            .productVariantId(variant.getProductVariantId())
                            .build())
                    .productVariant(variant)
                    .quantity(orderItemRequest.quantity())
                    .price(variant.getPrice())
                    .build();
            orderItems.add(orderItem);
        }


        BigDecimal shippingCharges = request.shippingCharge() != null ? request.shippingCharge() : BigDecimal.ZERO;
        BigDecimal totalAmount = subtotal.add(shippingCharges);
        String orderNumber = generateKey("ORDER", userId.toString(), true);

        List<TempOrderItem> tempOrderItems = orderMapper.toTempOrderItemsEntity(request.items());

        TempOrder tempOrder = TempOrder.builder()
                    .guestEmail(request.guestInfo().email())
                    .guestName(request.guestInfo().fullName())
                    .guestPhone(request.guestInfo().phone())
                    .shippingAddress(request.guestInfo().shippingAddress())
                    .city(request.guestInfo().city())
                    .district(request.guestInfo().district())
                    .ward(request.guestInfo().ward())
                    .orderItems(tempOrderItems)
                    .subtotal(subtotal)
                    .shippingCharges(shippingCharges)
                    .totalAmount(totalAmount)
                    .paymentStatus(TempOrder.PaymentStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusHours(24))
                    .idempotencyKey(request.idempotencyKey())
                    .orderNumber(orderNumber)
                    .build();

        TempOrder newTempOrder = tempOrderRepository.save(tempOrder);

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(newTempOrder.getId())
                .orderNumber(orderNumber)
                .subtotal(subtotal)
                .shippingCharges(shippingCharges)
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING)
                .build();

        return orderResponse;
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
