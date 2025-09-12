package com.bekassyl.ecommerceapp.repository;

import com.bekassyl.ecommerceapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
