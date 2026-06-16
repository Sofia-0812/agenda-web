package com.agenda.controller;

import com.agenda.model.ProfissionalSaude;
import com.agenda.repository.ProfissionalSaudeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profissionais")
@CrossOrigin(origins = "*")
public class ProfissionalSaudeController {

    private final ProfissionalSaudeRepository repository;

    public ProfissionalSaudeController(ProfissionalSaudeRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<ProfissionalSaude> inserir(@Valid @RequestBody ProfissionalSaude profissional) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(profissional));
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalSaude>> listar() {
        return ResponseEntity.ok(repository.findAllByOrderByNomeAsc());
    }

    @GetMapping("/buscar-nome")
    public ResponseEntity<List<ProfissionalSaude>> consultarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(repository.findByNomeContainingIgnoreCase(nome));
    }

    @GetMapping("/buscar-categoria")
    public ResponseEntity<List<ProfissionalSaude>> consultarPorCategoria(@RequestParam ProfissionalSaude.Categoria categoria) {
        return ResponseEntity.ok(repository.findByCategoria(categoria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalSaude> consultarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalSaude> alterar(@PathVariable Long id,
                                                      @Valid @RequestBody ProfissionalSaude dados) {
        return repository.findById(id)
                .map(p -> {
                    p.setNome(dados.getNome());
                    p.setTelefone(dados.getTelefone());
                    p.setEndereco(dados.getEndereco());
                    p.setCategoria(dados.getCategoria());
                    return ResponseEntity.ok(repository.save(p));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        return repository.findById(id)
                .map(p -> {
                    repository.delete(p);
                    return ResponseEntity.ok(Map.of("mensagem", "Profissional removido com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
