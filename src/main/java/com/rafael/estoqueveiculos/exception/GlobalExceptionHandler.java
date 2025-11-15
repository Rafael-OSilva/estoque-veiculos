// src/main/java/com/rafael/estoqueveiculos/exception/GlobalExceptionHandler.java
package com.rafael.estoqueveiculos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

// ⭐⭐ MUDANÇA: Import jakarta em vez de javax ⭐⭐
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ Captura erros de validação (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorMessage = "Erro de validação: " + errors.toString();
        ErrorResponse errorResponse = new ErrorResponse("ERRO_VALIDACAO", errorMessage, request.getRequestURI());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // ✅ Captura IllegalArgumentException (que você já usa)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse("ERRO_NEGOCIO", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // ✅ Captura EntityNotFoundException (nova exceção)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(
            EntityNotFoundException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse("NAO_ENCONTRADO", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // ✅ Captura BusinessException (nova exceção)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse("ERRO_NEGOCIO", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // ✅ Captura qualquer outra exceção não tratada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                "ERRO_INTERNO",
                "Ocorreu um erro interno no servidor: " + ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}