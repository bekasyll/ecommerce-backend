package com.bekassyl.ecommerceapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDTO {
    private Long id;
    private Long appUserId;
    private List<CartItemDTO> items;
}
