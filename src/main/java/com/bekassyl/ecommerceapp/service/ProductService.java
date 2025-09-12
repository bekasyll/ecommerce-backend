package com.bekassyl.ecommerceapp.service;

import com.bekassyl.ecommerceapp.exception.FileStorageException;
import com.bekassyl.ecommerceapp.exception.ResourceNotFoundException;
import com.bekassyl.ecommerceapp.model.Product;
import com.bekassyl.ecommerceapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    @Value("${upload.dir}")
    private String uploadDir;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    @Transactional
    public Product createProduct(Product product, MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            try {
                String filename = saveImage(image);
                product.setImage(filename);
            } catch (IOException e) {
                throw new FileStorageException("Failed to save image: " + image.getOriginalFilename());
            }
        }

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product updatedProduct, MultipartFile image) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());

        if (image != null && !image.isEmpty()) {
            String oldImage = existingProduct.getImage();

            try {
                String fileName = saveImage(image);
                existingProduct.setImage(fileName);

                if (oldImage != null) {
                    Path path = Paths.get(uploadDir, oldImage);
                    Files.deleteIfExists(path);
                }
            } catch (IOException e) {
                throw new FileStorageException("Failed to update image: " + image.getOriginalFilename());
            }
        }

        return productRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));

        productRepository.deleteById(id);

        if (product.getImage() != null) {
            try {
                Path imagePath = Paths.get(uploadDir, product.getImage());
                Files.deleteIfExists(imagePath);

            } catch (IOException e) {
                System.err.println("Could not delete image: " + product.getImage());
            }
        }
    }

    private String saveImage(MultipartFile image) throws IOException {
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path path = Paths.get(uploadDir, fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, image.getBytes());
        return fileName;
    }
}
