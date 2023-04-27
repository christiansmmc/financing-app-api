package com.greengoldfish.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

@ControllerAdvice
@AllArgsConstructor
public class BusinessExceptionHandler implements ProblemHandling, SecurityAdviceTrait  {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                e.getValue(),
                e.getStatus().getStatusCode()
        );

        return new ResponseEntity<>(apiException, HttpStatus.valueOf(e.getStatus().getStatusCode()));
    }
}
