package com.bekassyl.ecommerceapp.mapper;

import com.bekassyl.ecommerceapp.dto.CartDTO;
import com.bekassyl.ecommerceapp.dto.CartItemDTO;
import com.bekassyl.ecommerceapp.model.Cart;
import com.bekassyl.ecommerceapp.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "appUserId", source = "appUser.id")
    CartDTO toCartDTO(Cart cart);

    @Mapping(target = "appUser.id", source = "appUserId")
    Cart toCart(CartDTO cartDTO);

    @Mapping(target = "productId", source = "product.id")
    CartItemDTO toCartItemDTO(CartItem cartItem);

    @Mapping(target = "product.id", source = "productId")
    CartItem toCartItem(CartItemDTO cartItemDTO);
}
