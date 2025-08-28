package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.request.OrderItemRequest;
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

    @Mapping(target = "orderDiscounts", source = "discounts")
    @Mapping(target = "paymentMethod", source = "payment.paymentMethod")
    @Mapping(target = "paymentProvider", source = "payment.provider")
    UserOrderResponse toOrderResponseDTO(Order entity);
    Iterable<OrderResponse> toUserOrderResponseDTOs(List<Order> entities);


    @Mapping(target = "productVariantId", source = "productVariant.productVariantId")
    OrderItemResponse toOrderItemResponse(OrderItem entity);
    List<OrderItemResponse> toOrderItemResponses(List<OrderItem> entities);


    @Mapping(target = "code", source = "discount.code")
    @Mapping(target = "discription", source = "discount.description")
    OrderDiscountResponse toOrderDiscountResponse(OrderDiscount entity);
    Set<OrderDiscountResponse> toOrderDiscountResponses(Set<OrderDiscount> entities);

    List<OrderItem> toOrderItemsEntity(List<OrderItemRequest> orderItem);
    List<TempOrderItem> toTempOrderItemsEntity(List<OrderItemRequest> orderItem);


    @Mapping(target = "paymentMethod", source = "payment.paymentMethod")
    @Mapping(target = "paymentProvider", source = "payment.provider")
    GuestOrderResponse toOrderResponseDTO(TempOrder entity);
    Iterable<OrderResponse> toGuestOrderResponseDTOs(List<TempOrder> entities);


    @Mapping(target = "productVariantId", source = "productVariant.productVariantId")
    OrderItemResponse toOrderItemResponse(TempOrderItem entity);
    List<OrderItemResponse> toOrderItemResponsesFromTemp(List<TempOrderItem> entities);


    @Mapping(target = "orderItemId", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderItem tempOrderItemToOrderItem(TempOrderItem tempOrderItem);


    List<OrderItem> tempOrderToOrderItemsEntity(List<TempOrderItem> tempOrderItems);

}
