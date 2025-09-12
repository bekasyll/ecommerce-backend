package com.bekassyl.ecommerceapp.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    @NotBlank(message = "Address is required!")
    private String address;

    @NotBlank(message = "Phone number is required!")
    @Pattern(regexp = "\\+?[0-9]{10,15}")
    private String phoneNumber;
}
