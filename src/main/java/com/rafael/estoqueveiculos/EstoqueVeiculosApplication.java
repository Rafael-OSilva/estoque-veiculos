package com.rafael.estoqueveiculos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EstoqueVeiculosApplication {

    public static void main(String[] args) {
        SpringApplication.run(EstoqueVeiculosApplication.class, args);
        System.out.println("🚗 Sistema de Estoque de Veículos - Spring Boot Iniciado!");
    }
}