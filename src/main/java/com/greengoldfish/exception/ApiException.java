package com.greengoldfish.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiException {

    private final String detail;
    private final String code;
    private final int status;
}
