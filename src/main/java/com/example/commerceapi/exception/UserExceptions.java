package com.example.commerceapi.exception;

public class UserExceptions{

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String value) {
            super("User with '" + value + "' not found.");
        }
    }

    public static class  UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String value) {
            super("User with '" + value + "' already exists.");
        }
    }
}
