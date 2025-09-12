package com.bekassyl.ecommerceapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Email is required!")
    @Email
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password is required!")
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private Cart cart;

    public enum Role {
        USER, ADMIN
    }

    @Column(name = "email_confirmation", nullable = false)
    private boolean emailConfirmation;

    @Column(name = "confirmation_code", nullable = false)
    private String confirmationCode;
}