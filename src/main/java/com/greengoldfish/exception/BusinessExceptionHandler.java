package com.greengoldfish.exception;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@AllArgsConstructor
public class BusinessExceptionHandler {

    private static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
    private static final String ERROR_VALIDATION = "error.validation";

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                e.getValue(),
                e.getStatus().getStatusCode()
        );

        return new ResponseEntity<>(apiException, HttpStatus.valueOf(e.getStatus().getStatusCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result
                .getFieldErrors()
                .stream()
                .map(it ->
                        new FieldError(
                                it.getObjectName().replaceFirst("DTO$", ""),
                                it.getField(),
                                StringUtils.isNotBlank(it.getDefaultMessage())
                                        ? it.getDefaultMessage()
                                        : it.getCode()
                        )
                )
                .collect(Collectors.toList());

        RequestBodyValidationError requestBodyValidationError = new RequestBodyValidationError(
                BAD_REQUEST.value(),
                ERROR_VALIDATION,
                fieldErrors
        );

        return new ResponseEntity<>(requestBodyValidationError, BAD_REQUEST);
    }
}
