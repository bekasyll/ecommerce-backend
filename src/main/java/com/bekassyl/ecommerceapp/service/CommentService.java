package com.bekassyl.ecommerceapp.service;

import com.bekassyl.ecommerceapp.exception.ResourceNotFoundException;
import com.bekassyl.ecommerceapp.model.AppUser;
import com.bekassyl.ecommerceapp.model.Comment;
import com.bekassyl.ecommerceapp.model.Product;
import com.bekassyl.ecommerceapp.repository.AppUserRepository;
import com.bekassyl.ecommerceapp.repository.CommentRepository;
import com.bekassyl.ecommerceapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final AppUserRepository appUserRepository;
    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Comment addComment(Long productId, Long userId, Comment comment) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        AppUser appUser = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        comment.setProduct(product);
        comment.setAppUser(appUser);

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByProductId(Long productId) {
        return commentRepository.findByProductId(productId);
    }
}
