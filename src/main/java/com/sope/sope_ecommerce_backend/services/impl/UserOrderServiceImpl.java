package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.OrderCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.OrderItemRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserOrderCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.OrderResponse;
import com.sope.sope_ecommerce_backend.entities.*;
import com.sope.sope_ecommerce_backend.enums.DiscountScope;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.enums.PaymentMethod;
import com.sope.sope_ecommerce_backend.enums.PaymentStatus;
import com.sope.sope_ecommerce_backend.exception.CustomException;
import com.sope.sope_ecommerce_backend.mapper.OrderMapper;
import com.sope.sope_ecommerce_backend.repositories.OrderRepository;
import com.sope.sope_ecommerce_backend.repositories.ProductVariantRepository;
import com.sope.sope_ecommerce_backend.services.*;
import com.sope.sope_ecommerce_backend.services.patterns.OrderCreationStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.sope.sope_ecommerce_backend.utils.RandomUtil.generateKey;

@Service
@AllArgsConstructor
public class UserOrderServiceImpl implements OrderCreationStrategy<UserOrderCreateRequest> {

    private final UserService userService;
    private final CartService cartService;
    private final AddressService addressService;
    private final ProductVariantService productVariantService;
    private final OrderMapper orderMapper;

    private final OrderRepository orderRepository;

    private final DiscountService discountService;

    @Override
    public boolean supports(OrderCreateRequest request) {
        return request instanceof UserOrderCreateRequest;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(UserOrderCreateRequest request, UUID userId) {
        AppUser user = userService.getUserEntityById(userId);
        Address shippingAddress = addressService.getAddressEntityById(request.shippingAddressId());

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
                    .commissionFeePercent(variant.getProduct().getCategory().getCommissionFeePercent())
                    .build();
            orderItems.add(orderItem);
        }

        BigDecimal shippingCharges = request.shippingCharge() != null ? request.shippingCharge() : BigDecimal.ZERO;



        Order order = Order.builder()
                .appUser(user)
                .shippingAddress(shippingAddress)
                .orderDate(LocalDateTime.now())
                .subtotal(subtotal)
                .shippingCharges(shippingCharges)
                .note(request.note())
                .status(OrderStatus.PENDING)
                .orderItems(orderItems)
                .idempotencyKey(request.idempotencyKey())
                .statusHistory(new ArrayList<>())
                .build();

        CommissionEntity commission = CommissionEntity.builder()
                .commissionRate(new BigDecimal("0.05")) // Example commission rate of 5%
                .commissionAmount(subtotal.multiply(new BigDecimal("0.05"))) // Calculate commission amount
                .recordedAt(LocalDateTime.now())
                .order(order)
                .build();

        order.setCommission(commission);


        // Apply discount
        BigDecimal discountOnOrder = BigDecimal.ZERO;
        BigDecimal discountOnShipping = BigDecimal.ZERO;
        Set<OrderDiscount> discounts = new HashSet<>();
        if (request.discountCodes() != null && !request.discountCodes().isEmpty()) {
            for (String discountCode : request.discountCodes()) {
                Discount discount = discountService.getDiscountByCode(discountCode)
                        .orElseThrow(() -> new CustomException("Invalid discount code: " + discountCode));

                BigDecimal discountValueForOrder = discountService.applyDiscount(discount, subtotal, shippingCharges);


                if (discount.getScope() == DiscountScope.FREESHIP) {
                    discountOnShipping = discountOnShipping.add(discountValueForOrder);
                } else {
                    discountOnOrder = discountOnOrder.add(discountValueForOrder);
                }

                OrderDiscount orderDiscount = OrderDiscount.builder()
                        .order(order)
                        .discount(discount)
                        .discountName(discount.getScope().name())
                        .discountAmount(discountValueForOrder)
                        .build();

                discounts.add(orderDiscount);
            }
        }

        BigDecimal subtotalAfterDiscount = subtotal.subtract(discountOnOrder);
        BigDecimal shippingAfterDiscount = shippingCharges.subtract(discountOnShipping);

        if (shippingAfterDiscount.compareTo(BigDecimal.ZERO) < 0) shippingAfterDiscount = BigDecimal.ZERO;
        if (subtotalAfterDiscount.compareTo(BigDecimal.ZERO) < 0) subtotalAfterDiscount = BigDecimal.ZERO;

        BigDecimal totalAmount = subtotalAfterDiscount.add(shippingAfterDiscount);

        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            totalAmount = BigDecimal.ZERO;
        }

        Payment payment = Payment.builder()
                .order(order)
                .amount(totalAmount)
                .paymentMethod(request.paymentMethod())
                .status(PaymentStatus.PENDING)
                .idempotencyKey(request.idempotencyKey())
                .build();

        if( request.paymentMethod() != PaymentMethod.COD ) {
            order.setExpireAt(LocalDateTime.now().plusHours(24));
        }

        order.setPayment(payment);
        order.setDiscounts(discounts);
        order.setTotalAmount(totalAmount);
        order.setOrderNumber(generateKey("ORDER", userId.toString(), true));

        orderItems.forEach(item ->
                {
                    item.setOrder(order);
                    item.getOrderItemId().setOrderId(order.getOrderId());

                    productVariantService.retrieveProductVariantStock(
                            item.getProductVariant().getProductVariantId(),
                            -item.getQuantity()
                    );
                }
        );

        addStatusHistory(order, OrderStatus.PENDING);

//        cartService.clearCart(cart);
        return orderMapper.toOrderResponseDTO(orderRepository.save(order));
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
