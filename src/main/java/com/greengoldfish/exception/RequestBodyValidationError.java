package com.greengoldfish.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public class RequestBodyValidationError implements Serializable {

    private int status;
    private String message;
    private List<FieldError> fieldErrors;
}
