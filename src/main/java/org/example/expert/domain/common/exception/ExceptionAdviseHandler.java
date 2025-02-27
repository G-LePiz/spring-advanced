package org.example.expert.domain.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdviseHandler {

    @ExceptionHandler(CommonExceptions.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CommonExceptions e){
        log.error("code : {} / message : {}", e.getMessage(), e.getExceptionStatus());
        return new ResponseEntity(new ErrorResponseDto(e.getExceptionStatus().getMessage(), e.getExceptionStatus().getErrorCode()), e.getExceptionStatus().getErrorCode());
    }
}
