package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.OrderCreateRequest;
import com.sope.sope_ecommerce_backend.dto.request.OrderItemRequest;
import com.sope.sope_ecommerce_backend.dto.request.ShopOrderRequest;
import com.sope.sope_ecommerce_backend.dto.request.UserOrderCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.OrderResponse;
import com.sope.sope_ecommerce_backend.dto.response.ShopResponse;
import com.sope.sope_ecommerce_backend.entities.*;
import com.sope.sope_ecommerce_backend.enums.DiscountScope;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.enums.PaymentMethod;
import com.sope.sope_ecommerce_backend.enums.PaymentStatus;
import com.sope.sope_ecommerce_backend.exception.CustomException;
import com.sope.sope_ecommerce_backend.mapper.OrderMapper;
import com.sope.sope_ecommerce_backend.repositories.OrderRepository;
import com.sope.sope_ecommerce_backend.repositories.PaymentRepository;
import com.sope.sope_ecommerce_backend.repositories.ProductVariantRepository;
import com.sope.sope_ecommerce_backend.services.*;
import com.sope.sope_ecommerce_backend.services.patterns.OrderCreationStrategy;
import com.sope.sope_ecommerce_backend.utils.IdempotencyUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.sope.sope_ecommerce_backend.utils.RandomUtil.generateKey;

@Slf4j
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

    private final ShopService shopService;

    private final PaymentRepository paymentRepository;

    @Override
    public boolean supports(OrderCreateRequest request) {
        return request instanceof UserOrderCreateRequest;
    }

    @Override
    @Transactional
    public List<? extends OrderResponse> createOrder(UserOrderCreateRequest request, UUID userId) {
        // 1. Lấy địa chỉ giao hàng + user
        AppUser user = userService.getUserEntityById(userId);
        Address shippingAddress = addressService.getAddressEntityById(request.shippingAddressId());

        // List<Order> ordersToSave = new ArrayList<>();
        List<OrderItem> allOrderItems = new ArrayList<>();

        // 2. Build order cho từng shop
        List<Order> ordersToSave = request.shopOrders().stream()
                .map(shopOrder -> buildOrder(shopOrder, user, shippingAddress, request, allOrderItems, userId))
                .toList();

        // 3. Batch update stock
        batchUpdateStock(allOrderItems);

        // 4. Nếu online payment → gộp payment chung
        if (request.paymentMethod() != PaymentMethod.COD) {
            BigDecimal grandTotal = ordersToSave.stream()
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Payment sharedPayment = Payment.builder()
                    .amount(grandTotal)
                    .paymentMethod(request.paymentMethod())
                    .provider(request.paymentProvider())
                    .status(PaymentStatus.PENDING)
                    .build();

            sharedPayment.setOrders(ordersToSave);

            List<Order> persistedOrders = IdempotencyUtils.saveWithIdempotency(
                    () -> paymentRepository.save(sharedPayment).getOrders(),
                    () -> Optional.of(orderRepository.findAllByIdempotencyKeyContaining(request.idempotencyKey())),
                    new RuntimeException("Order not found after duplicate key"));

            return persistedOrders.stream()
                    .map(orderMapper::toOrderResponseDTO)
                    .toList();
        }

        // 6. Apply idempotency
        List<Order> persistedOrders = IdempotencyUtils.saveWithIdempotency(
                () -> orderRepository.saveAll(ordersToSave),
                () -> Optional.of(orderRepository.findAllByIdempotencyKeyContaining(request.idempotencyKey())),
                new RuntimeException("Order not found after duplicate key"));

        return persistedOrders.stream()
                .map(orderMapper::toOrderResponseDTO)
                .toList();
    }

    private Order buildOrder(ShopOrderRequest shopOrder,
            AppUser user,
            Address shippingAddress,
            UserOrderCreateRequest request,
            List<OrderItem> allOrderItems,
            UUID userId) {

        Shop shop = shopService.getShopEntityById(shopOrder.shopId());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal subTotal = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : shopOrder.items()) {
            ProductVariant variant = productVariantService.getProductVariantEntityById(itemReq.productVariantId());
            if (variant.getStock() < itemReq.quantity() || variant.getStock() <= 0) {
                throw new CustomException("Insufficient stock for " + variant.getProduct().getName());
            } else if (itemReq.quantity() <= 0) {
                throw new CustomException("Quantity must be greater than zero for " + variant.getProduct().getName());
            } else if (!variant.getProduct().getShop().getId().equals(shop.getId())) {
                throw new CustomException(
                        "Product " + variant.getProduct().getName() + " does not belong to shop " + shop.getName());

            }

            OrderItem orderItem = OrderItem.builder()
                    .productVariant(variant)
                    .quantity(itemReq.quantity())
                    .price(variant.getPrice())
                    .commissionFeePercent(variant.getProduct().getCategory().getCommissionFeePercent())
                    .build();

            orderItems.add(orderItem);
            allOrderItems.add(orderItem);

            BigDecimal itemPrice = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            subTotal = subTotal.add(itemPrice);
        }

        BigDecimal shippingCharges = shopOrder.shippingCharge() != null ? shopOrder.shippingCharge() : BigDecimal.ZERO;

        // === Apply Discounts ===
        BigDecimal discountOnOrder = BigDecimal.ZERO;
        BigDecimal discountOnShipping = BigDecimal.ZERO;
        Set<OrderDiscount> discounts = new HashSet<>();

        if (shopOrder.discountCodes() != null && !shopOrder.discountCodes().isEmpty()) {
            for (String discountCode : shopOrder.discountCodes()) {
                Discount discount = discountService.getDiscountEntityByCode(discountCode);
                BigDecimal discountValue = discountService.applyDiscount(discount, subTotal, shippingCharges);

                if (discount.getScope() == DiscountScope.FREESHIP) {
                    discountOnShipping = discountOnShipping.add(discountValue);
                } else {
                    discountOnOrder = discountOnOrder.add(discountValue);
                }

                OrderDiscount orderDiscount = OrderDiscount.builder()
                        .order(null)
                        .discount(discount)
                        .discountName(discount.getScope().name())
                        .discountAmount(discountValue)
                        .build();

                discounts.add(orderDiscount);
            }
        }

        BigDecimal subtotalAfterDiscount = subTotal.subtract(discountOnOrder);
        BigDecimal shippingAfterDiscount = shippingCharges.subtract(discountOnShipping);

        if (subtotalAfterDiscount.compareTo(BigDecimal.ZERO) < 0)
            subtotalAfterDiscount = BigDecimal.ZERO;
        if (shippingAfterDiscount.compareTo(BigDecimal.ZERO) < 0)
            shippingAfterDiscount = BigDecimal.ZERO;

        BigDecimal totalAmount = subtotalAfterDiscount.add(shippingAfterDiscount);
        if (totalAmount.compareTo(BigDecimal.ZERO) < 0)
            totalAmount = BigDecimal.ZERO;

        // === Build Order ===
        Order order = Order.builder()
                .appUser(user)
                .shop(shop)
                .shippingAddress(shippingAddress)
                .orderDate(LocalDateTime.now())
                .subTotal(subTotal)
                .shippingCharges(shippingCharges)
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING)
                .note(shopOrder.note())
                .discounts(discounts)
                .statusHistory(new ArrayList<>())
                .orderNumber(generateKey("ORDER", userId.toString(), true))
                .orderItems(orderItems)
                .shippingRateId(shopOrder.shippingRateId())
                .idempotencyKey(request.idempotencyKey() + "-" + shop.getId())
                .build();

        // === Payment ===
        if (request.paymentMethod() == PaymentMethod.COD) {
            Payment codPayment = Payment.builder()
                    .orders(List.of(order))
                    .amount(totalAmount)
                    .paymentMethod(PaymentMethod.COD)
                    .status(PaymentStatus.PENDING)
                    .build();
            order.setPayment(codPayment);
        } else {
            order.setExpireAt(LocalDateTime.now().plusHours(24));
        }

        // Gắn 2 chiều cho orderItem + discount
        orderItems.forEach(item -> {
            item.setOrder(order);
            item.setOrderItemId(
                    OrderItemId.builder()
                            .orderId(order.getOrderId())
                            .productVariantId(item.getProductVariant().getProductVariantId())
                            .build());
        });
        discounts.forEach(d -> d.setOrder(order));

        // === Xử lý cart ===
        if (request.isOrderedFromCart() && shopOrder.items() != null) {
            List<UUID> productVariantIds = shopOrder.items().stream()
                    .map(OrderItemRequest::productVariantId)
                    .toList();

            cartService.removeItemsFromCart(userId, productVariantIds);
        }

        addStatusHistory(order, OrderStatus.PENDING);
        return order;
    }

    private void batchUpdateStock(List<OrderItem> allOrderItems) {
        Map<UUID, Integer> stockUpdates = new HashMap<>();
        for (OrderItem item : allOrderItems) {
            stockUpdates.merge(
                    item.getProductVariant().getProductVariantId(),
                    item.getQuantity(),
                    Integer::sum);
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
