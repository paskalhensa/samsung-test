package org.example.exceptions;

public class ClientInputException extends RuntimeException {
    public ClientInputException(String message) {
        super(message);
    }
}
