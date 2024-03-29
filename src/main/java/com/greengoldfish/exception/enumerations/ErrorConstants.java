package com.greengoldfish.exception.enumerations;

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
    },
    DATES_CANNOT_BE_NULL {
        @Override
        public String getValue() {
            return "error.dates.cannot.be.null";
        }
        @Override
        public String getMessage() {
            return "The initial date and last date param cannot be null";
        }
    },
    TRANSACTION_NOT_FROM_USER {
        @Override
        public String getValue() {
            return "error.transaction.not.from.user";
        }
        @Override
        public String getMessage() {
            return "The informed transaction is not from user";
        }
    },
    CREDIT_CARD_NOT_FROM_USER {
        @Override
        public String getValue() {
            return "error.credit.card.not.from.user";
        }
        @Override
        public String getMessage() {
            return "The informed credit card is not from logged user";
        }
    },
    ;

    public abstract String getValue();
    public abstract String getMessage();
}
