package com.rafael.estoqueveiculos.service;

import com.rafael.estoqueveiculos.model.Marca;
import com.rafael.estoqueveiculos.model.Modelo;
import com.rafael.estoqueveiculos.repository.MarcaRepository;
import com.rafael.estoqueveiculos.repository.ModeloRepository;
import com.rafael.estoqueveiculos.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModeloService {

    private final ModeloRepository modeloRepository;
    private final MarcaRepository marcaRepository;
    private final VeiculoRepository veiculoRepository;

    // --- CORREÇÃO AQUI ---
    // O construtor agora Pede o VeiculoRepository para o Spring
    public ModeloService(ModeloRepository modeloRepository,
                         MarcaRepository marcaRepository,
                         VeiculoRepository veiculoRepository) { // <-- 1. ADICIONADO O PARÂMETRO

        this.modeloRepository = modeloRepository;
        this.marcaRepository = marcaRepository;
        this.veiculoRepository = veiculoRepository; // <-- 2. AGORA A ATRIBUIÇÃO FUNCIONA
    }

    public List<Modelo> listarTodos() {
        return modeloRepository.findAll();
    }

    public List<Modelo> listarPorMarca(Long marcaId) {
        return modeloRepository.findByMarcaId(marcaId);
    }

    public List<Modelo> listarPorMarcaNome(String nome) {
        return modeloRepository.findByMarcaNomeIgnoreCase(nome);
    }

    public Modelo salvar(Modelo modelo) {

        if (modelo.getMarca() == null || modelo.getMarca().getId() == null) {
            throw new IllegalArgumentException("A marca deve ser informada.");
        }

        Marca marca = marcaRepository.findById(modelo.getMarca().getId())
                .orElseThrow(() -> new IllegalArgumentException("Marca informada não existe."));

        modelo.setMarca(marca);

        // Se for novo, valida duplicidade; se for edição, permite manter o mesmo nome
        if (modelo.getId() == null) {
            if (modeloRepository.existsByNomeIgnoreCaseAndMarcaId(modelo.getNome(), marca.getId())) {
                throw new IllegalArgumentException("Modelo já existe para esta marca.");
            }
        } else {
            // edição: se existe outro modelo com mesmo nome nessa marca, bloquear
            Optional<Modelo> existente = modeloRepository.findAll().stream()
                    .filter(m -> m.getNome().equalsIgnoreCase(modelo.getNome()) && m.getMarca().getId().equals(marca.getId()))
                    .filter(m -> !m.getId().equals(modelo.getId()))
                    .findAny();
            if (existente.isPresent()) {
                throw new IllegalArgumentException("Outro modelo com esse nome já existe para esta marca.");
            }
        }

        return modeloRepository.save(modelo);
    }

    public boolean existe(Long id) {
        return modeloRepository.existsById(id);
    }

    public void deletar(Long id) {
        // Agora o veiculoRepository não é mais null
        if (veiculoRepository.existsByModeloId(id)) {
            throw new IllegalArgumentException(
                    "Não é possível excluir o modelo, pois existem veículos vinculados a ele."
            );
        }
        modeloRepository.deleteById(id);
    }
}