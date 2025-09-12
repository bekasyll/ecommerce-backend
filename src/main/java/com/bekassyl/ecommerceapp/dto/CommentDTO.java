package com.bekassyl.ecommerceapp.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;

    @NotBlank(message = "Content is required!")
    @Size(message = "Content must be between 1 and 1000 characters!")
    private String content;

    @NotNull(message = "Score is required!")
    @Min(value = 1, message = "Min. value = 1")
    @Max(value = 5, message = "Max. value = 5")
    private Integer score;

    private Long appUserId;
}
