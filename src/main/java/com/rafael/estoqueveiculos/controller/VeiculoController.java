package com.rafael.estoqueveiculos.controller;

import com.rafael.estoqueveiculos.model.Veiculo;
import com.rafael.estoqueveiculos.service.VeiculoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/veiculos")
@CrossOrigin(origins = "*")
public class VeiculoController {

    private final VeiculoService service;

    public VeiculoController(VeiculoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Veiculo> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> buscar(@PathVariable Long id) {
        Veiculo v = service.buscarPorId(id);
        return v != null ? ResponseEntity.ok(v) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Veiculo veiculo) {
        return ResponseEntity.ok(service.salvar(veiculo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Veiculo veiculo) {
        return ResponseEntity.ok(service.atualizar(id, veiculo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        boolean ok = service.excluir(id);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/relatorio/status")
    public ResponseEntity<?> relatorioStatus() {
        long disponiveis = service.contarVeiculosPorStatus("disponível");
        long vendidos = service.contarVeiculosPorStatus("vendido");
        long reservados = service.contarVeiculosPorStatus("reservado");
        long total = service.listarTodos().size();

        Map<String, Object> rel = new HashMap<>();
        rel.put("disponiveis", disponiveis);
        rel.put("vendidos", vendidos);
        rel.put("reservados", reservados);
        rel.put("total", total);

        return ResponseEntity.ok(rel);
    }

    @GetMapping("/relatorio/valor-estoque")
    public ResponseEntity<?> relatorioValorEstoque() {
        double valor = service.calcularValorTotalEstoque();
        Map<String, Object> rel = new HashMap<>();
        rel.put("valorTotal", valor);
        return ResponseEntity.ok(rel);
    }
}
