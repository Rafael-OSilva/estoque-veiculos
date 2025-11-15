package com.rafael.estoqueveiculos.repository;

import com.rafael.estoqueveiculos.model.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModeloRepository extends JpaRepository<Modelo, Long> {

    boolean existsByNomeIgnoreCaseAndMarcaId(String nome, Long marcaId);

    List<Modelo> findByMarcaId(Long marcaId);

    List<Modelo> findByMarcaNomeIgnoreCase(String nome);

    boolean existsByMarcaId(Long marcaId);
}
