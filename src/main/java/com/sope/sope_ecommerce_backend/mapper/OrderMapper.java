package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.response.*;
import com.sope.sope_ecommerce_backend.entities.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // Ánh xạ từ Order sang UserOrderResponse
    @Mapping(target = "shippingAddress", source = "shippingAddress")
    @Mapping(target = "order", source = ".")
    UserOrderResponse toOrderResponseDTO(Order entity);

    List<OrderResponse> toUserOrderResponseDTOs(List<Order> entities);

    // Ánh xạ từ Order sang ShopOrderResponse
    @Mapping(target = "shopInfo", source = "shop")
    @Mapping(target = "orderDiscounts", source = "discounts")
    @Mapping(target = "paymentMethod", source = "payment.paymentMethod")
    @Mapping(target = "paymentProvider", source = "payment.provider")
    @Mapping(target = "paymentStatus", source = "payment.status")
    ShopOrderResponse toShopOrderResponse(Order entity);

    // Ánh xạ từ TempOrder sang GuestOrderResponse
    @Mapping(target = "guestInfo.fullName", source = "guestName")
    @Mapping(target = "guestInfo.email", source = "guestEmail")
    @Mapping(target = "guestInfo.phone", source = "guestPhone")
    @Mapping(target = "guestInfo.shippingAddress", source = "shippingAddress")
    @Mapping(target = "guestInfo.city", source = "city")
    @Mapping(target = "guestInfo.district", source = "district")
    @Mapping(target = "guestInfo.ward", source = "ward")
    @Mapping(target = "order", source = ".")
    GuestOrderResponse toOrderResponseDTO(TempOrder entity);

    List<OrderResponse> toGuestOrderResponseDTOs(List<TempOrder> entities);

    // Ánh xạ từ TempOrder sang ShopOrderResponse
    @Mapping(target = "shopInfo", source = "shop")
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "orderDiscounts", ignore = true)
    @Mapping(target = "paymentMethod", source = "payment.paymentMethod")
    @Mapping(target = "paymentProvider", source = "payment.provider")
    @Mapping(target = "paymentStatus", source = "payment.status")
    @Mapping(target = "items", source = "orderItems")
    ShopOrderResponse toShopOrderResponse(TempOrder entity);

    // Ánh xạ OrderItem
    @Mapping(target = "productVariantId", source = "productVariant.productVariantId")
    OrderItemResponse toOrderItemResponse(OrderItem entity);

    List<OrderItemResponse> toOrderItemResponses(List<OrderItem> entities);

    // Ánh xạ TempOrderItem
    @Mapping(target = "productVariantId", source = "productVariant.productVariantId")
    OrderItemResponse toOrderItemResponse(TempOrderItem entity);

    List<OrderItemResponse> toOrderItemResponsesFromTemp(List<TempOrderItem> entities);

    // Ánh xạ OrderDiscount
    @Mapping(target = "code", source = "discount.code")
    @Mapping(target = "description", source = "discount.description")
    @Mapping(target = "discountAmount", source = "discountAmount")
    OrderDiscountResponse toOrderDiscountResponse(OrderDiscount entity);

    Set<OrderDiscountResponse> toOrderDiscountResponses(Set<OrderDiscount> entities);

    // Chuyển đổi TempOrderItem sang OrderItem
    @Mapping(target = "orderItemId", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderItem tempOrderItemToOrderItem(TempOrderItem tempOrderItem);

    List<OrderItem> tempOrderToOrderItemsEntity(List<TempOrderItem> tempOrderItems);


}