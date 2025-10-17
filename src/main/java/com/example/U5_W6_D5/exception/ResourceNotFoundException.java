package com.example.U5_W6_D5.exception;

/**
 * Eccezione lanciata quando una risorsa richiesta non viene trovata.
 * Mappata su HTTP 404 NOT FOUND
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s non trovato con ID: %d", resourceName, id));
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s non trovato con %s: %s", resourceName, fieldName, fieldValue));
    }
}

