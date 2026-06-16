package com.agenda;

import com.agenda.model.ProfissionalSaude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProfissionalSaudeControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    public void deveCriarProfissional() throws Exception {
        ProfissionalSaude p = new ProfissionalSaude();
        p.setNome("Dr. João");
        p.setTelefone("(31) 99999-0000");
        p.setCategoria(ProfissionalSaude.Categoria.MEDICO);

        mockMvc.perform(post("/api/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Dr. João"));
    }

    @Test
    public void deveListarProfissionais() throws Exception {
        mockMvc.perform(get("/api/profissionais"))
                .andExpect(status().isOk());
    }
}
