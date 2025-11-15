package com.rafael.estoqueveiculos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "modelos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Modelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do modelo é obrigatório")
    @Size(min = 1, max = 100, message = "Nome deve ter entre 1 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotNull(message = "Marca é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Marca marca;

    public Modelo() {}
    public Modelo(String nome, Marca marca) { this.nome = nome; this.marca = marca; }
    public Modelo(Long id, String nome, Marca marca) { this.id = id; this.nome = nome; this.marca = marca; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Marca getMarca() { return marca; }
    public void setMarca(Marca marca) { this.marca = marca; }

    @Override
    public String toString() {
        return "Modelo{" + "id=" + id + ", nome='" + nome + '\'' + ", marca=" + (marca != null ? marca.getNome() : "null") + '}';
    }
}
