// src/main/java/com/rafael/estoqueveiculos/exception/ErrorResponse.java
package com.rafael.estoqueveiculos.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class ErrorResponse {
    private String codigo;
    private String mensagem;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    // Construtor sem path
    public ErrorResponse(String codigo, String mensagem) {
        this.codigo = codigo;
        this.mensagem = mensagem;
        this.timestamp = LocalDateTime.now();
    }

    // Construtor com path (opcional)
    public ErrorResponse(String codigo, String mensagem, String path) {
        this.codigo = codigo;
        this.mensagem = mensagem;
        this.timestamp = LocalDateTime.now();
    }

    // Getters e Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}