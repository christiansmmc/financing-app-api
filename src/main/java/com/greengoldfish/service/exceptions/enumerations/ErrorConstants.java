package com.greengoldfish.service.exceptions.enumerations;

public enum ErrorConstants {
    GENERAL {
        @Override
        public String getValue() {
            return "error.general";
        }

        @Override
        public String getMessage() {
            return "general error";
        }
    },
    USER_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.user.not.found";
        }
        @Override
        public String getMessage() {
            return "The informed user is not found";
        }
    },
    TRANSACTION_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.transaction.not.found";
        }
        @Override
        public String getMessage() {
            return "The informed transaction is not found";
        }
    },
    AUTHORITY_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.authority.not.found";
        }
        @Override
        public String getMessage() {
            return "The informed authority is not found";
        }
    },
    EMAIL_ALREADY_REGISTERED {
        @Override
        public String getValue() {
            return "error.email.already.registered";
        }
        @Override
        public String getMessage() {
            return "The informed email is already registered";
        }
    };

    public abstract String getValue();
    public abstract String getMessage();
}
