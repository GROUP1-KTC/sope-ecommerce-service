package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.request.OrderItemRequest;
import com.sope.sope_ecommerce_backend.dto.response.CartItemResponseDTO;
import com.sope.sope_ecommerce_backend.dto.response.OrderItemResponse;
import com.sope.sope_ecommerce_backend.dto.response.OrderResponse;
import com.sope.sope_ecommerce_backend.entities.CartItem;
import com.sope.sope_ecommerce_backend.entities.Order;
import com.sope.sope_ecommerce_backend.entities.OrderItem;
import com.sope.sope_ecommerce_backend.entities.TempOrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toOrderResponseDTO(Order entity);
    Iterable<OrderResponse> toOrderResponseDTOs(List<Order> entities);


    List<OrderItem> toOrderItemsEntity(List<OrderItemRequest> orderItem);

    List<TempOrderItem> toTempOrderItemsEntity(List<OrderItemRequest> orderItem);

    List<OrderItem> tempOrderToOrderItemsEntity(List<TempOrderItem> tempOrderItems);

}
