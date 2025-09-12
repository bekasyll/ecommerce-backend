package com.bekassyl.ecommerceapp.controller;

import com.bekassyl.ecommerceapp.dto.OrderDTO;
import com.bekassyl.ecommerceapp.mapper.OrderMapper;
import com.bekassyl.ecommerceapp.model.Order;
import com.bekassyl.ecommerceapp.request.CreateOrderRequest;
import com.bekassyl.ecommerceapp.security.AppUserDetails;
import com.bekassyl.ecommerceapp.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders()
                .stream()
                .map(orderMapper::toOrderDTO)
                .toList();

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderDTO>> getAppUserOrders(@AuthenticationPrincipal UserDetails userDetails) {
        Long appUserId = ((AppUserDetails) userDetails).getAppUser().getId();
        List<OrderDTO> orders = orderService.getAppUserOrders(appUserId)
                .stream()
                .map(orderMapper::toOrderDTO)
                .toList();

        return ResponseEntity.ok(orders);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDTO> createOrder(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestBody @Valid CreateOrderRequest request) {
        Long appUserId = ((AppUserDetails) userDetails).getAppUser().getId();
        OrderDTO orderDTO = orderMapper.toOrderDTO(orderService.createOrder(appUserId, request.getAddress(), request.getPhoneNumber()));

        return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable("orderId") Long orderId,
                                                      @RequestParam Order.OrderStatus status) {
        OrderDTO updatedOrderDTO = orderMapper.toOrderDTO(orderService.updateOrderStatus(orderId, status));
        return ResponseEntity.ok(updatedOrderDTO);
    }
}
