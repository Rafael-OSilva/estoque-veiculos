// src/main/java/com/rafael/estoqueveiculos/exception/EntityNotFoundException.java
package com.rafael.estoqueveiculos.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entityName, Long id) {
        super(entityName + " com ID " + id + " não encontrada!");
    }
}