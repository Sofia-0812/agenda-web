package com.agenda;

import com.agenda.controller.ExameLaboratorioController;
import com.agenda.model.ExameLaboratorio;
import com.agenda.repository.ExameLaboratorioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExameLaboratorioController.class)
class ExameLaboratorioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExameLaboratorioRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarExameComSucesso() throws Exception {
        ExameLaboratorio exame = new ExameLaboratorio();
        exame.setId(1L);
        exame.setDescricao("Hemograma Completo");
        exame.setPosologia("Jejum de 8 horas");

        when(repository.save(any(ExameLaboratorio.class))).thenReturn(exame);

        mockMvc.perform(post("/api/exames")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exame)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Hemograma Completo"))
                .andExpect(jsonPath("$.posologia").value("Jejum de 8 horas"));
    }

    @Test
    void deveRetornar404ParaExameInexistente() throws Exception {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/exames/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDeletarExameComSucesso() throws Exception {
        ExameLaboratorio exame = new ExameLaboratorio();
        exame.setId(1L);
        exame.setDescricao("Exame de Urina");

        when(repository.findById(1L)).thenReturn(Optional.of(exame));

        mockMvc.perform(delete("/api/exames/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Exame removido com sucesso"));
    }
}