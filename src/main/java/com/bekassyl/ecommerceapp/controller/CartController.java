package com.bekassyl.ecommerceapp.controller;

import com.bekassyl.ecommerceapp.dto.CartDTO;
import com.bekassyl.ecommerceapp.mapper.CartMapper;
import com.bekassyl.ecommerceapp.security.AppUserDetails;
import com.bekassyl.ecommerceapp.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final CartMapper cartMapper;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        Long appUserId = ((AppUserDetails) userDetails).getAppUser().getId();
        CartDTO cartDTO = cartMapper.toCartDTO(cartService.getCart(appUserId));

        return ResponseEntity.ok(cartDTO);
    }

    @PostMapping(value = "/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDTO> addToCart(@AuthenticationPrincipal UserDetails userDetails,
                                             @RequestParam Long productId, @RequestParam Integer quantity) {
        System.out.println("Hi!");
        Long appUserId = ((AppUserDetails) userDetails).getAppUser().getId();
        CartDTO cartDTO = cartMapper.toCartDTO(cartService.addToCart(appUserId, productId, quantity));

        return ResponseEntity.ok(cartDTO);
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        Long appUserId = ((AppUserDetails) userDetails).getAppUser().getId();
        cartService.clearCart(appUserId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> removeCartItem(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable("productId") Long productId) {
        Long appUserId = ((AppUserDetails) userDetails).getAppUser().getId();
        cartService.removeCartItem(appUserId, productId);

        return ResponseEntity.noContent().build();
    }
}
