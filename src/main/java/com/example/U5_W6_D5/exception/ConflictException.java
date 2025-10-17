package com.example.U5_W6_D5.exception;

/**
 * Eccezione lanciata quando si verifica un conflitto, ad esempio sovrapposizione di date.
 * Mappata su HTTP 409 CONFLICT
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}

