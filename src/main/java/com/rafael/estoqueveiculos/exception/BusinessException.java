// src/main/java/com/rafael/estoqueveiculos/exception/BusinessException.java
package com.rafael.estoqueveiculos.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}