package com.greengoldfish.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class FieldError implements Serializable {

    private final String objectName;

    private final String field;

    private final String message;
}
