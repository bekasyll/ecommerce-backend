package com.bekassyl.ecommerceapp.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Name is required!")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 chars!")
    private String name;

    @NotBlank(message = "Description is required!")
    @Size(max = 1000, message = "Description must not exceed 1000 chars!")
    private String description;

    @NotNull(message = "Price is required!")
    @PositiveOrZero(message = "Price cannot be negative!")
    private BigDecimal price;

    @NotNull(message = "Quantity is required!")
    @PositiveOrZero(message = "Quantity cannot be negative!")
    private Integer quantity;

    @Size(max = 255, message = "Image name must not exceed 255 chars!")
    private String image;
    private List<CommentDTO> comments;
}
