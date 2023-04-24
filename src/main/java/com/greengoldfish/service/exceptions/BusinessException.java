package com.greengoldfish.service.exceptions;

import com.greengoldfish.service.exceptions.enumerations.ErrorConstants;
import com.greengoldfish.service.exceptions.enumerations.NotFoundConstant;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

import java.util.function.Supplier;

import static org.zalando.problem.Status.BAD_REQUEST;
import static org.zalando.problem.Status.NOT_FOUND;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {
    private final String value;
    private final String message;
    private final StatusType status;

    private BusinessException(Class<?> entityClass) {
        super(NotFoundConstant.ENTITY_NOT_FOUND.getValue(entityClass));
        var error = NotFoundConstant.ENTITY_NOT_FOUND;
        this.message = error.getMessage(entityClass);
        this.value = error.getValue(entityClass);
        this.status = NOT_FOUND;
    }

    private BusinessException(ErrorConstants errorConstants, Status status) {
        super(errorConstants.getValue());
        this.value = errorConstants.getValue();
        this.message = errorConstants.getMessage();
        this.status = status;
    }

    private static void throwIf(
            Boolean conditional,
            ErrorConstants errorConstants,
            Status status
    ) {
        if (Boolean.TRUE.equals(conditional)) {
            throw new BusinessException(errorConstants, status);
        }
    }

    public static Supplier<BusinessException> notFound(ErrorConstants error) {
        return () -> new BusinessException(error, NOT_FOUND);
    }

    public static Supplier<BusinessException> notFound(Class<?> entityClass) {
        return () -> new BusinessException(entityClass);
    }

    public static Supplier<BusinessException> badRequest(ErrorConstants error) {
        return () -> new BusinessException(error, BAD_REQUEST);
    }

    public static void throwIf(Boolean conditional, ErrorConstants errorConstants) {
        BusinessException.throwIf(conditional, errorConstants, BAD_REQUEST);
    }

    public static void throwIfNot(Boolean conditional, ErrorConstants errorConstants) {
        BusinessException.throwIf(BooleanUtils.negate(conditional), errorConstants, BAD_REQUEST);
    }
}
