package com.bekassyl.ecommerceapp.service;

import com.bekassyl.ecommerceapp.exception.InsufficientStockException;
import com.bekassyl.ecommerceapp.exception.ResourceNotFoundException;
import com.bekassyl.ecommerceapp.model.AppUser;
import com.bekassyl.ecommerceapp.model.Cart;
import com.bekassyl.ecommerceapp.model.CartItem;
import com.bekassyl.ecommerceapp.model.Product;
import com.bekassyl.ecommerceapp.repository.AppUserRepository;
import com.bekassyl.ecommerceapp.repository.CartRepository;
import com.bekassyl.ecommerceapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AppUserRepository appUserRepository;

    @Transactional
    public Cart getCart(Long appUserId) {
        return cartRepository.findByAppUserId(appUserId)
                .orElseGet(() -> createCartForAppUser(appUserId));
    }

    @Transactional
    public void clearCart(Long appUserId) {
        Cart cart = cartRepository.findByAppUserId(appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found!"));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Transactional
    public void removeCartItem(Long appUserId, Long productId) {
        Cart cart = cartRepository.findByAppUserId(appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found!"));

        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cartRepository.save(cart);
    }

    @Transactional
    public Cart addToCart(Long appUserId, Long productId, Integer quantity) {
        AppUser appUser = appUserRepository.findById(appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));

        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException("Not enough available!");
        }

        Cart cart = cartRepository.findByAppUserId(appUserId).orElse(new Cart(null, appUser, new ArrayList<>()));
        Optional<CartItem> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId)).findFirst();

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem(null, cart, product, quantity);
            cart.getItems().add(cartItem);
        }
        return cartRepository.save(cart);
    }

    private Cart createCartForAppUser(Long appUserId) {
        AppUser appUser = appUserRepository.findById(appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        Cart cart = new Cart(null, appUser, new ArrayList<>());
        return cartRepository.save(cart);
    }
}
