package com.bekassyl.ecommerceapp.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailConfirmationRequest {
    @NotBlank(message = "Email is required!")
    private String email;

    @NotBlank(message = "Code is required!")
    private String code;
}
