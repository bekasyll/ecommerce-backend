package com.bekassyl.ecommerceapp.controller;

import com.bekassyl.ecommerceapp.request.AuthRequest;
import com.bekassyl.ecommerceapp.model.AppUser;
import com.bekassyl.ecommerceapp.request.ChangePasswordRequest;
import com.bekassyl.ecommerceapp.request.EmailConfirmationRequest;
import com.bekassyl.ecommerceapp.response.ApiResponse;
import com.bekassyl.ecommerceapp.response.JwtResponse;
import com.bekassyl.ecommerceapp.security.JwtUtil;
import com.bekassyl.ecommerceapp.service.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AppUserService appUserService;

    @PostMapping("/registration")
    public ResponseEntity<ApiResponse> register(@RequestBody @Valid AuthRequest authRequest) {
        AppUser appUser = new AppUser();
        appUser.setEmail(authRequest.getEmail());
        appUser.setPassword(authRequest.getPassword());

        appUserService.registerUser(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("User registered successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid AuthRequest authRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(), authRequest.getPassword()
        );

        try {
            authenticationManager.authenticate(authToken);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        String jwt = jwtUtil.generateToken(authRequest.getEmail());

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        appUserService.changePassword(email, request);

        return ResponseEntity.ok(new ApiResponse("Password was changed!"));
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<ApiResponse> confirmEmail(@RequestBody EmailConfirmationRequest request) {
        System.out.println(request.getCode());
        appUserService.confirmEmail(request.getEmail(), request.getCode());
        return ResponseEntity.ok(new ApiResponse("Email confirmed successfully!"));
    }
}
