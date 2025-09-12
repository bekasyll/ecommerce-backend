package com.bekassyl.ecommerceapp.repository;

import com.bekassyl.ecommerceapp.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByAppUserId(Long userId);
}
