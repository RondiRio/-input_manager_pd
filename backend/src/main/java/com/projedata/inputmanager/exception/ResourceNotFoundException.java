package com.projedata.inputmanager.exception;

// PT-BR: Excecao lancada quando um recurso solicitado nao existe no banco de dados.
//        Mapeada para HTTP 404 pelo GlobalExceptionHandler.
// EN-US: Exception thrown when a requested resource does not exist in the database.
//        Mapped to HTTP 404 by the GlobalExceptionHandler.
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " with id " + id + " not found");
    }
}
