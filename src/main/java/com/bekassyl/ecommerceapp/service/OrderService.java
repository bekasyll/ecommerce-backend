package com.bekassyl.ecommerceapp.service;

import com.bekassyl.ecommerceapp.exception.EmailNotConfirmedException;
import com.bekassyl.ecommerceapp.exception.InsufficientStockException;
import com.bekassyl.ecommerceapp.exception.ResourceNotFoundException;
import com.bekassyl.ecommerceapp.model.*;
import com.bekassyl.ecommerceapp.repository.AppUserRepository;
import com.bekassyl.ecommerceapp.repository.OrderRepository;
import com.bekassyl.ecommerceapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AppUserRepository appUserRepository;
    private final CartService cartService;
    private final EmailService emailService;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getAppUserOrders(Long appUserId) {
        return orderRepository.findByAppUserId(appUserId);
    }

    @Transactional
    public Order createOrder(Long appUserId, String address, String phoneNumber) {
        AppUser appUser = appUserRepository.findById(appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if (!appUser.isEmailConfirmation()) {
            throw new EmailNotConfirmedException("Email not confirmed! Please confirm your email before placing order.");
        }

        Cart cart = cartService.getCart(appUserId);
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create an order with an empty cart!");
        }

        Order order = new Order();
        order.setAppUser(appUser);
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);
        order.setStatus(Order.OrderStatus.PREPARING);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = createOrderItems(cart, order);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(appUserId);

        try {
            emailService.sendOrderConfirmation(savedOrder);
        } catch (MailException e) {
            logger.error("Failed to send order confirmation email for order ID " + savedOrder.getId(), e);
        }
        return savedOrder;
    }

    private List<OrderItem> createOrderItems(Cart cart, Order order) {
        return cart.getItems().stream().map(cartItem -> {
            Product product = productRepository.findById(cartItem.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + cartItem.getProduct().getId()));

            if (product.getQuantity() == null) {
                throw new IllegalStateException("Product quantity is not set for product " + product.getName());
            }
            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new InsufficientStockException("Not enough stock for product " + product.getName());
            }

            product.setQuantity(product.getQuantity() - cartItem.getQuantity());

            return new OrderItem(null, product, order, cartItem.getQuantity(), product.getPrice());
        }).toList();
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, Order.OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found!"));

        order.setStatus(orderStatus);
        logger.info("Order {} status updated to {}", orderId, orderStatus);

        return orderRepository.save(order);
    }
}
