package com.bekassyl.ecommerceapp.controller;

import com.bekassyl.ecommerceapp.dto.ProductDTO;
import com.bekassyl.ecommerceapp.mapper.ProductMapper;
import com.bekassyl.ecommerceapp.model.Product;
import com.bekassyl.ecommerceapp.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products =  productService.getAllProducts()
                .stream()
                .map(productMapper::toProductDTO)
                .toList();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productMapper.toProductDTO(productService.getProductById(id)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> createProduct(@RequestPart("product") @Valid ProductDTO productDTO,
                                                    @RequestPart(value = "image", required = false) MultipartFile image) {
        Product product = productService.createProduct(productMapper.toProduct(productDTO), image);

        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toProductDTO(product));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") Long id, @RequestPart("product") @Valid ProductDTO productDTO,
                                                    @RequestPart(value = "image", required = false) MultipartFile image) {
        Product product = productService.updateProduct(id, productMapper.toProduct(productDTO), image);

        return ResponseEntity.ok(productMapper.toProductDTO(product));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}
