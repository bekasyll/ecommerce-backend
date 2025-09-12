package com.bekassyl.ecommerceapp.mapper;

import com.bekassyl.ecommerceapp.dto.CommentDTO;
import com.bekassyl.ecommerceapp.dto.ProductDTO;
import com.bekassyl.ecommerceapp.model.Comment;
import com.bekassyl.ecommerceapp.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "image", source = "image")
    ProductDTO toProductDTO(Product product);

    @Mapping(target = "image", source = "image")
    Product toProduct(ProductDTO productDTO);

    @Mapping(target = "appUserId", source = "appUser.id")
    CommentDTO toCommentDTO(Comment comment);

    @Mapping(target = "appUser.id", source = "appUserId")
    @Mapping(target = "product", ignore = true)
    Comment toComment(CommentDTO commentDTO);
}
