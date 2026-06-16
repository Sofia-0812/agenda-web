package com.agenda.controller;

import com.agenda.model.ExameLaboratorio;
import com.agenda.repository.ExameLaboratorioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exames")
@CrossOrigin(origins = "*")
public class ExameLaboratorioController {

    private final ExameLaboratorioRepository repository;

    public ExameLaboratorioController(ExameLaboratorioRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<ExameLaboratorio> criar(@Valid @RequestBody ExameLaboratorio exame) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(exame));
    }

    @GetMapping
    public ResponseEntity<List<ExameLaboratorio>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExameLaboratorio> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/atendimento/{atendimentoId}")
    public ResponseEntity<List<ExameLaboratorio>> buscarPorAtendimento(@PathVariable Long atendimentoId) {
        return ResponseEntity.ok(repository.findByAtendimentoId(atendimentoId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExameLaboratorio> atualizar(@PathVariable Long id,
                                                       @Valid @RequestBody ExameLaboratorio dados) {
        return repository.findById(id)
                .map(e -> {
                    e.setDescricao(dados.getDescricao());
                    e.setPosologia(dados.getPosologia());
                    e.setAtendimento(dados.getAtendimento());
                    return ResponseEntity.ok(repository.save(e));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return repository.findById(id)
                .map(e -> {
                    repository.delete(e);
                    return ResponseEntity.ok(Map.of("mensagem", "Exame removido com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
