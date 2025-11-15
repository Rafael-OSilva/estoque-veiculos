package com.rafael.estoqueveiculos.controller;

import com.rafael.estoqueveiculos.model.Modelo;
import com.rafael.estoqueveiculos.service.ModeloService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modelos")
@CrossOrigin(origins = "*")
public class ModeloController {

    private final ModeloService service;

    public ModeloController(ModeloService service) {
        this.service = service;
    }

    @GetMapping
    public List<Modelo> listar() {
        return service.listarTodos();
    }

    @GetMapping("/marca/{id}")
    public List<Modelo> porMarca(@PathVariable Long id) {
        return service.listarPorMarca(id);
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Modelo modelo) {
        return ResponseEntity.ok(service.salvar(modelo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Modelo modelo) {
        modelo.setId(id);
        return ResponseEntity.ok(service.salvar(modelo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        if (!service.existe(id)) {
            return ResponseEntity.notFound().build();
        }
        service.deletar(id);
        return ResponseEntity.ok().build();
    }
}
