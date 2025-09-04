package com.sope.sope_ecommerce_backend.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sope.sope_ecommerce_backend.dto.request.*;
import com.sope.sope_ecommerce_backend.dto.response.OrderResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserOrderResponse;
import com.sope.sope_ecommerce_backend.entities.*;

import com.sope.sope_ecommerce_backend.enums.DiscountScope;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.enums.PaymentMethod;
import com.sope.sope_ecommerce_backend.enums.PaymentStatus;
import com.sope.sope_ecommerce_backend.exception.CustomException;
import com.sope.sope_ecommerce_backend.mapper.OrderMapper;
import com.sope.sope_ecommerce_backend.repositories.PaymentRepository;
import com.sope.sope_ecommerce_backend.repositories.TempOrderRepository;
import com.sope.sope_ecommerce_backend.services.AddressService;
import com.sope.sope_ecommerce_backend.services.ProductVariantService;
import com.sope.sope_ecommerce_backend.services.ShopService;
import com.sope.sope_ecommerce_backend.services.UserService;
import com.sope.sope_ecommerce_backend.services.patterns.OrderCreationStrategy;
import com.sope.sope_ecommerce_backend.utils.IdempotencyUtils;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ShopService shopService;
    private final PaymentRepository paymentRepository;


    @Override
    public boolean supports(OrderCreateRequest request) {
        return request instanceof GuestOrderCreateRequest;
    }

    @Override
    @Transactional
    public List<OrderResponse> createOrder(GuestOrderCreateRequest request, UUID userId) {
        String idempotencyKey = request.idempotencyKey();

        if (request.guestInfo() == null || request.guestInfo().fullName() == null
                || request.guestInfo().phone() == null) {
            throw new IllegalArgumentException("Guest info incomplete");
        }

        if (request.paymentMethod() == PaymentMethod.COD) {
            throw new IllegalArgumentException("Guest cannot pay by COD");
        }

        if (request.shopOrders() == null || request.shopOrders().isEmpty()) {
            throw new IllegalArgumentException("No shop orders provided");
        }

        List<TempOrderItem> allOrderItems = new ArrayList<>();


        List<TempOrder> tempOrdersToSave = request.shopOrders().stream()
                .map(shopOrder -> buildOrder(shopOrder, request, allOrderItems))
                .toList();

        batchUpdateStock(allOrderItems);

        BigDecimal grandTotal = tempOrdersToSave.stream()
                .map(TempOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        Payment sharedPayment = Payment.builder()
                .amount(grandTotal)
                .paymentMethod(request.paymentMethod())
                .provider(request.paymentProvider())
                .status(PaymentStatus.PENDING)
                .build();

        sharedPayment.setTempOrders(tempOrdersToSave);

        List<TempOrder> savedTempOrders = IdempotencyUtils.saveWithIdempotency(
                        () -> paymentRepository.save(sharedPayment).getTempOrders(),
                        () -> Optional.of(tempOrderRepository.findAllByIdempotencyKeyContaining(request.idempotencyKey())),
                        new RuntimeException("TempOrder not found after duplicate key")
                );


        return orderMapper.toGuestOrderResponseDTOs(savedTempOrders);
    }


    private TempOrder buildOrder(ShopOrderRequest shopOrder,
                             GuestOrderCreateRequest request,
                             List<TempOrderItem> allOrderItems) {

        Shop shop = shopService.getShopEntityById(shopOrder.shopId());


        List<TempOrderItem> orderItems = new ArrayList<>();
        BigDecimal subTotal = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : shopOrder.items()) {
            ProductVariant variant = productVariantService.getProductVariantEntityById(itemReq.productVariantId());
            if (variant.getStock() < itemReq.quantity() || variant.getStock() <= 0) {
                throw new CustomException("Insufficient stock for " + variant.getProduct().getName());
            } else if (itemReq.quantity() <= 0) {
                throw new CustomException("Quantity must be greater than zero for " + variant.getProduct().getName());
            } else if (!variant.getProduct().getShop().getId().equals(shop.getId())) {
                throw new CustomException("Product " + variant.getProduct().getName() + " does not belong to shop " + shop.getName());

            }

            TempOrderItem orderItem = TempOrderItem.builder()
                    .productVariant(variant)
                    .quantity(itemReq.quantity())
                    .price(variant.getPrice())
                    .build();

            orderItems.add(orderItem);
            allOrderItems.add(orderItem);

            BigDecimal itemPrice =  orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            subTotal = subTotal.add(itemPrice);
        }

        BigDecimal shippingCharges = shopOrder.shippingCharge() != null ? shopOrder.shippingCharge() : BigDecimal.ZERO;


        BigDecimal totalAmount = subTotal.add(shippingCharges);
        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) totalAmount = BigDecimal.ZERO;

        // === Build Order ===
        TempOrder tempOrder = TempOrder.builder()
                .guestEmail(request.guestInfo().email())
                .guestName(request.guestInfo().fullName())
                .guestPhone(request.guestInfo().phone())
                .shippingAddress(request.guestInfo().shippingAddress())
                .city(request.guestInfo().city())
                .district(request.guestInfo().district())
                .ward(request.guestInfo().ward())
                .subTotal(subTotal)
                .shippingCharges(shippingCharges)
                .totalAmount(totalAmount)
                .paymentStatus(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .idempotencyKey(request.idempotencyKey() + "-" + shop.getId())
                .shop(shop)
                .orderNumber(generateKey(
                        "ORDER",
                        UUID.randomUUID().toString(),
                        true
                ))
                .shippingRateId(shopOrder.shippingRateId())
                .orderItems(orderItems)
                .build();

        return tempOrder;
    }


    private void batchUpdateStock(List<TempOrderItem> allOrderItems) {
        Map<UUID, Integer> stockUpdates = new HashMap<>();
        for (TempOrderItem item : allOrderItems) {
            stockUpdates.merge(
                    item.getProductVariant().getProductVariantId(),
                    item.getQuantity(),
                    Integer::sum
            );
        }
        productVariantService.updateStockBatch(stockUpdates);
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
