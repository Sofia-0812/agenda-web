package com.agenda;

import com.agenda.controller.ProfissionalSaudeController;
import com.agenda.model.ProfissionalSaude;
import com.agenda.repository.ProfissionalSaudeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfissionalSaudeController.class)
public class ProfissionalSaudeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfissionalSaudeRepository repository;

    @Test
    public void deveCriarProfissional() throws Exception {
        ProfissionalSaude p = new ProfissionalSaude();
        p.setId(1L);
        p.setNome("Dr. João");
        p.setTelefone("(31) 99999-0000");
        p.setCategoria(ProfissionalSaude.Categoria.MEDICO);

        when(repository.save(any())).thenReturn(p);

        mockMvc.perform(post("/api/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Dr. João"));
    }

    @Test
    public void deveListarProfissionais() throws Exception {
        when(repository.findAllByOrderByNomeAsc()).thenReturn(List.of());

        mockMvc.perform(get("/api/profissionais"))
                .andExpect(status().isOk());
    }
}