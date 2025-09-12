package com.bekassyl.ecommerceapp.mapper;

import com.bekassyl.ecommerceapp.dto.OrderDTO;
import com.bekassyl.ecommerceapp.dto.OrderItemDTO;
import com.bekassyl.ecommerceapp.model.Order;
import com.bekassyl.ecommerceapp.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "appUserId", source = "appUser.id")
    @Mapping(target = "orderItems", source = "orderItems")
    OrderDTO toOrderDTO(Order order);

    @Mapping(target = "appUser.id", source = "appUserId")
    @Mapping(target = "orderItems", source = "orderItems")
    Order toOrder(OrderDTO orderDTO);

    @Mapping(target = "productId", source = "product.id")
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);

    @Mapping(target = "product.id", source = "productId")
    OrderItem toOrderItem(OrderItemDTO orderItemDTO);
}
