package com.rafael.estoqueveiculos.controller;

import com.rafael.estoqueveiculos.model.Marca;
import com.rafael.estoqueveiculos.service.MarcaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
@CrossOrigin(origins = "*")
public class MarcaController {

    private final MarcaService service;

    public MarcaController(MarcaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Marca> listar() {
        return service.listarTodas();
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Marca marca) {
        return ResponseEntity.ok(service.salvar(marca));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.ok().build();
    }
}
