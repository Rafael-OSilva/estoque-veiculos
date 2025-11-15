package com.rafael.estoqueveiculos.service;

import com.rafael.estoqueveiculos.model.Modelo;
import com.rafael.estoqueveiculos.model.Veiculo;
import com.rafael.estoqueveiculos.repository.ModeloRepository;
import com.rafael.estoqueveiculos.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final ModeloRepository modeloRepository;

    public VeiculoService(
            VeiculoRepository veiculoRepository,
            ModeloRepository modeloRepository
    ) {
        this.veiculoRepository = veiculoRepository;
        this.modeloRepository = modeloRepository;
    }

    public List<Veiculo> listarTodos() {
        return veiculoRepository.findAll();
    }

    public Veiculo buscarPorId(Long id) {
        return veiculoRepository.findById(id).orElse(null);
    }

    public Veiculo salvar(Veiculo veiculo) {
        validarEPrepararVeiculo(veiculo);
        return veiculoRepository.save(veiculo);
    }

    public Veiculo atualizar(Long id, Veiculo veiculoAtualizado) {
        Veiculo existente = veiculoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado"));


        if (veiculoAtualizado.getModelo() != null && veiculoAtualizado.getModelo().getId() != null) {
            Modelo modelo = modeloRepository.findById(veiculoAtualizado.getModelo().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Modelo não encontrado."));
            existente.setModelo(modelo);
        }

        if (veiculoAtualizado.getAno() != null) existente.setAno(veiculoAtualizado.getAno());
        if (veiculoAtualizado.getCor() != null) existente.setCor(veiculoAtualizado.getCor());
        if (veiculoAtualizado.getPreco() != null) existente.setPreco(veiculoAtualizado.getPreco());
        if (veiculoAtualizado.getQuilometragem() != null) existente.setQuilometragem(veiculoAtualizado.getQuilometragem());
        if (veiculoAtualizado.getStatus() != null) existente.setStatus(veiculoAtualizado.getStatus());

        validarEPrepararVeiculo(existente);

        return veiculoRepository.save(existente);
    }

    public boolean excluir(Long id) {
        if (!veiculoRepository.existsById(id)) return false;
        Veiculo v = veiculoRepository.findById(id).get();
        if ("vendido".equalsIgnoreCase(v.getStatus())) {
            throw new IllegalArgumentException("Não é possível excluir veículos vendidos!");
        }
        veiculoRepository.deleteById(id);
        return true;
    }

    // Relatórios
    public long contarVeiculosPorStatus(String status) {
        return veiculoRepository.findByStatusIgnoreCase(status).size();
    }

    public double calcularValorTotalEstoque() {
        List<Veiculo> disponiveis = veiculoRepository.findByStatusIgnoreCase("disponível");
        return disponiveis.stream().mapToDouble(Veiculo::getPreco).sum();
    }

    // Validação / preparação
    private void validarEPrepararVeiculo(Veiculo veiculo) {
        if (veiculo.getModelo() == null || veiculo.getModelo().getId() == null)
            throw new IllegalArgumentException("Modelo é obrigatório!");

        // Carregar entidades completas
        Modelo modelo = modeloRepository.findById(veiculo.getModelo().getId())
                .orElseThrow(() -> new IllegalArgumentException("Modelo não encontrado."));

        veiculo.setModelo(modelo);

        if (veiculo.getAno() == null || veiculo.getAno() < 1900 || veiculo.getAno() > 2100)
            throw new IllegalArgumentException("Ano inválido.");

        if (veiculo.getPreco() == null || veiculo.getPreco() <= 0)
            throw new IllegalArgumentException("Preço inválido.");

        if (veiculo.getCor() == null || veiculo.getCor().trim().isEmpty())
            throw new IllegalArgumentException("Cor é obrigatória.");

        // Normaliza status: mantém acento se o frontend enviar assim, apenas lower case trim
        if (veiculo.getStatus() == null || veiculo.getStatus().trim().isEmpty())
            veiculo.setStatus("disponível");
        else
            veiculo.setStatus(veiculo.getStatus().trim().toLowerCase());
    }
}
