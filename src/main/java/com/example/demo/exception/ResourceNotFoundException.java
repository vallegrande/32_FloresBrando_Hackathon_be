package com.example.demo.exception;

/**
 * Se lanza cuando se busca un registro por id (o clave) y no existe en la base de datos.
 * El GlobalExceptionHandler la traduce a un HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
