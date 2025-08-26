package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.request.OrderItemRequest;
import com.sope.sope_ecommerce_backend.dto.response.CartItemResponseDTO;
import com.sope.sope_ecommerce_backend.dto.response.OrderItemResponse;
import com.sope.sope_ecommerce_backend.dto.response.OrderResponse;
import com.sope.sope_ecommerce_backend.entities.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toOrderResponseDTO(Order entity);
    Iterable<OrderResponse> toOrderResponseDTOs(List<Order> entities);


    List<OrderItem> toOrderItemsEntity(List<OrderItemRequest> orderItem);

    List<TempOrderItem> toTempOrderItemsEntity(List<OrderItemRequest> orderItem);


    @Mapping(target = "orderItemId", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderItem tempOrderItemToOrderItem(TempOrderItem tempOrderItem);


    List<OrderItem> tempOrderToOrderItemsEntity(List<TempOrderItem> tempOrderItems);

}
