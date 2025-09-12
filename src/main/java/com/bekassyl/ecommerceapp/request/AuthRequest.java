package com.bekassyl.ecommerceapp.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @NotBlank(message = "Email is required!")
    @Email
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 8, max = 30, message = "Password must be between 8 and 255 chars!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
