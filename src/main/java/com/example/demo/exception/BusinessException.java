package com.example.demo.exception;

/**
 * Se lanza cuando se viola una regla de negocio (duplicados, matrícula repetida, etc).
 * El GlobalExceptionHandler la traduce a un HTTP 400.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
