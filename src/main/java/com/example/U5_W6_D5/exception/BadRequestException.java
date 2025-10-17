package com.example.U5_W6_D5.exception;

/**
 * Eccezione lanciata quando la richiesta contiene dati non validi.
 * Mappata su HTTP 400 BAD REQUEST
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
