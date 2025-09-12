package com.bekassyl.ecommerceapp.mapper;

import com.bekassyl.ecommerceapp.dto.CommentDTO;
import com.bekassyl.ecommerceapp.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "appUserId", source = "appUser.id")
    CommentDTO toCommentDTO(Comment comment);

    @Mapping(target = "appUser.id", source = "appUserId")
    @Mapping(target = "product", ignore = true)
    Comment toComment(CommentDTO commentDTO);
}
