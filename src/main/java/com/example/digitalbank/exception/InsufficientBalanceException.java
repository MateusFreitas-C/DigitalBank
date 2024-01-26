package com.example.digitalbank.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String type) {
        super("Insufficient " + type + " to carry out this transaction");
    }
}
