package com.rafael.estoqueveiculos.repository;

import com.rafael.estoqueveiculos.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    List<Veiculo> findByStatusIgnoreCase(String status);
    boolean existsByModeloId(Long modeloId);
}
