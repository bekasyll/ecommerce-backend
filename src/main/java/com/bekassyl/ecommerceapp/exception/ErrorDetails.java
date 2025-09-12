package com.bekassyl.ecommerceapp.exception;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
    private String message;
    private LocalDateTime timestamp;
}
