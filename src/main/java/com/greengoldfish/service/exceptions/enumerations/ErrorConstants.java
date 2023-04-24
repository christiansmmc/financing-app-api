package com.greengoldfish.service.exceptions.enumerations;

public enum ErrorConstants {
    USER_NOT_FOUND {
        @Override
        public String getMessage() {
            return "The informed user is not found";
        }
    },
    TRANSACTION_NOT_FOUND {
        @Override
        public String getMessage() {
            return "The informed transaction is not found";
        }
    },
    LOGIN_ALREADY_REGISTERED {
        @Override
        public String getMessage() {
            return "The informed login is already registered";
        }
    };

    public abstract String getMessage();
}
