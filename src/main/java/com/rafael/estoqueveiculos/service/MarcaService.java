package com.rafael.estoqueveiculos.service;

import com.rafael.estoqueveiculos.model.Marca;
import com.rafael.estoqueveiculos.repository.MarcaRepository;
import com.rafael.estoqueveiculos.repository.ModeloRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarcaService {

    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;

    public MarcaService(MarcaRepository marcaRepository, ModeloRepository modeloRepository) {
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
    }

    public List<Marca> listarTodas() {
        return marcaRepository.findAll();
    }

    public Marca salvar(Marca marca) {

        if (marca.getNome() == null || marca.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da marca é obrigatório.");
        }

        if (marcaRepository.existsByNomeIgnoreCase(marca.getNome())) {
            throw new IllegalArgumentException("A marca '" + marca.getNome() + "' já existe.");
        }

        marca.setNome(marca.getNome().trim());
        return marcaRepository.save(marca);
    }

    public void excluir(Long id) {

        if (modeloRepository.existsByMarcaId(id)) {
            throw new IllegalArgumentException(
                    "Não é possível excluir a marca pois existem modelos vinculados a ela."
            );
        }

        try {
            marcaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Erro: existem veículos ou modelos relacionados a esta marca.");
        }
    }
}
