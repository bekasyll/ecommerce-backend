package com.bekassyl.ecommerceapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long id;
    private Long productId;

    @NotNull(message = "Quantity is required!")
    @Positive
    private Integer quantity;

    @NotNull(message = "Price is required!")
    @Positive
    private BigDecimal price;
}
