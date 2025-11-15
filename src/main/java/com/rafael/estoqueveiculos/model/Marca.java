package com.rafael.estoqueveiculos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "marcas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome da marca é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    public Marca() {}

    public Marca(String nome) { this.nome = nome; }

    public Marca(Long id, String nome) { this.id = id; this.nome = nome; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    @Override
    public String toString() {
        return "Marca{" + "id=" + id + ", nome='" + nome + '\'' + '}';
    }
}
