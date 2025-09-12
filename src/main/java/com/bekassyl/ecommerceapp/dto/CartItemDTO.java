package com.bekassyl.ecommerceapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;
    private Long productId;

    @NotNull(message = "Quantity is required!")
    @Positive
    private Integer quantity;
}
