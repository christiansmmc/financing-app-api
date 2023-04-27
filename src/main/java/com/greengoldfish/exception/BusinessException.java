package com.greengoldfish.config.exception;

import com.greengoldfish.service.exceptions.enumerations.ErrorConstants;
import com.greengoldfish.service.exceptions.enumerations.NotFoundConstant;
import lombok.Getter;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.helpers.MessageFormatter;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

import java.util.function.Supplier;

import static org.zalando.problem.Status.BAD_REQUEST;
import static org.zalando.problem.Status.NOT_FOUND;

@Getter
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

    public BusinessException(ErrorConstants errorConstants, Status status) {
        super(errorConstants.getValue());
        this.value = errorConstants.getValue();
        this.message = errorConstants.getMessage();
        this.status = status;
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
        this.value = null;
        this.status = null;
    }

    public static Supplier<BusinessException> notFound(Class<?> entityClass) {
        return () -> new BusinessException(entityClass);
    }

    public static Supplier<BusinessException> notFound(String message, Object... args) {
        return () -> new BusinessException(MessageFormatter.arrayFormat(message, args).getMessage());
    }

    public static Supplier<BusinessException> by(ErrorConstants errorConstants) {
        return () -> new BusinessException(errorConstants, BAD_REQUEST);
    }

    public static void throwIf(
            Boolean conditional,
            ErrorConstants errorConstants,
            Status status
    ) {
        if (Boolean.TRUE.equals(conditional)) {
            throw new BusinessException(errorConstants, status);
        }
    }

    public static void throwIf(Boolean conditional, ErrorConstants errorConstants) {
        BusinessException.throwIf(conditional, errorConstants, BAD_REQUEST);
    }

    public static void throwIfNot(
            Boolean conditional,
            ErrorConstants errorConstants,
            Status status
    ) {
        BusinessException.throwIf(BooleanUtils.negate(conditional), errorConstants, status);
    }

    public static void throwIfNot(Boolean conditional, ErrorConstants errorConstants) {
        BusinessException.throwIf(BooleanUtils.negate(conditional), errorConstants);
    }
}
