package com.rafael.estoqueveiculos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "veiculos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Modelo é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Modelo modelo;

    @NotNull(message = "Ano é obrigatório")
    @Min(value = 1900, message = "Ano deve ser maior ou igual a 1900")
    @Max(value = 2100, message = "Ano deve ser menor ou igual a 2100")
    @Column(nullable = false)
    private Integer ano;

    @NotBlank(message = "Cor é obrigatória")
    @Size(min = 2, max = 30, message = "Cor deve ter entre 2 e 30 caracteres")
    @Column(nullable = false, length = 30)
    private String cor;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser maior que zero")
    @Column(nullable = false)
    private Double preco;

    @PositiveOrZero(message = "Quilometragem não pode ser negativa")
    @Column
    private Double quilometragem;

    // Aceita formas com ou sem acento e case-insensitive
    @NotBlank(message = "Status é obrigatório")
    @Pattern(
            regexp = "(?i)dispon[íi]vel|vendido|reservado",
            message = "Status deve ser: disponível, vendido ou reservado"
    )
    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    public Veiculo() {
        this.dataCadastro = LocalDateTime.now();
        this.status = "disponível";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Modelo getModelo() { return modelo; }
    public void setModelo(Modelo modelo) { this.modelo = modelo; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }

    public Double getQuilometragem() { return quilometragem; }
    public void setQuilometragem(Double quilometragem) { this.quilometragem = quilometragem; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}
