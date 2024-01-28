package com.example.digitalbank.exception;

public class SelfTransactionException extends RuntimeException{
    public SelfTransactionException() {
        super("You cannot send money to yourself.");
    }
}
