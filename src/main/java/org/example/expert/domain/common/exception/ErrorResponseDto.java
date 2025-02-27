package org.example.expert.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private final String message;
    private final HttpStatus errorCode;
}
