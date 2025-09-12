package com.bekassyl.ecommerceapp.dto;

import com.bekassyl.ecommerceapp.model.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long appUserId;

    @NotBlank(message = "Address is required!")
    private String address;

    @NotBlank(message = "Phone number is required!")
    private String phoneNumber;

    @NotNull(message = "Status is required!")
    private Order.OrderStatus status;

    @NotNull
    @CreationTimestamp
    private LocalDateTime createdAt;

    private List<OrderItemDTO> orderItems;
}
