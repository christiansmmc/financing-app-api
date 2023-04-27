package com.greengoldfish.service.exceptions.enumerations;

public enum NotFoundConstant {
    ENTITY_NOT_FOUND;

    public String getValue(Class<?> entityClass) {
        return "error." + entityClass.getSimpleName().toLowerCase() + ".not.found";
    }

    public String getMessage(Class<?> entityClass) {
        return entityClass.getSimpleName() + " not found";
    }
}
