package com.bekassyl.ecommerceapp.controller;

import com.bekassyl.ecommerceapp.dto.CommentDTO;
import com.bekassyl.ecommerceapp.mapper.CommentMapper;
import com.bekassyl.ecommerceapp.model.AppUser;
import com.bekassyl.ecommerceapp.model.Comment;
import com.bekassyl.ecommerceapp.security.AppUserDetails;
import com.bekassyl.ecommerceapp.service.CommentService;
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
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByProduct(@PathVariable("productId") Long id) {
        List<CommentDTO> comments = commentService.getCommentsByProductId(id)
                .stream()
                .map(commentMapper::toCommentDTO)
                .toList();

        return ResponseEntity.ok(comments);
    }

    @PostMapping("/product/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDTO> addComment(@PathVariable("productId") Long productId,
                                                 @AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestBody @Valid CommentDTO commentDTO) {
        AppUser appUser = ((AppUserDetails) userDetails).getAppUser();
        Comment comment = commentService.addComment(productId, appUser.getId(), commentMapper.toComment(commentDTO));

        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.toCommentDTO(comment));
    }
}
