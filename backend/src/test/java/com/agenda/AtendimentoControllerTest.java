package com.agenda;

import com.agenda.controller.AtendimentoController;
import com.agenda.model.Atendimento;
import com.agenda.repository.AtendimentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AtendimentoController.class)
class AtendimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AtendimentoRepository repository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        // Necessário para o Jackson conseguir mapear LocalDate e LocalTime do Atendimento
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void deveCriarAtendimentoComSucesso() throws Exception {
        Atendimento atendimento = new Atendimento();
        atendimento.setId(1L);
        atendimento.setTitulo("Consulta de Rotina");
        atendimento.setData(LocalDate.of(2026, 6, 20));
        atendimento.setHorario(LocalTime.of(14, 30));

        when(repository.save(any(Atendimento.class))).thenReturn(atendimento);

        mockMvc.perform(post("/api/atendimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atendimento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Consulta de Rotina"));
    }

    @Test
    void deveListarAtendimentosOrdenados() throws Exception {
        Atendimento a1 = new Atendimento();
        a1.setId(1L);
        a1.setTitulo("Teleconsulta A");

        Atendimento a2 = new Atendimento();
        a2.setId(2L);
        a2.setTitulo("Consulta Presencial B");

        when(repository.findAllByOrderByDataAscHorarioAsc())
                .thenReturn(Arrays.asList(a1, a2));

        mockMvc.perform(get("/api/atendimentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Teleconsulta A"))
                .andExpect(jsonPath("$[1].titulo").value("Consulta Presencial B"));
    }

    @Test
    void deveRetornar404ParaAtendimentoInexistente() throws Exception {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/atendimentos/999"))
                .andExpect(status().isNotFound());
    }
}