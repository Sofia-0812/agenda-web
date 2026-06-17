package com.agenda;

import com.agenda.model.ProfissionalSaude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class IntegracaoTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void deveExecutarFluxoCompletoDoProfissional() throws Exception {
        // 1. INSERIR Profissional de Saúde
        ProfissionalSaude p = new ProfissionalSaude();
        p.setNome("Dr. Augusto Lima");
        p.setTelefone("(31) 98888-5555");
        p.setEndereco("Av. Afonso Pena, 2000");
        p.setCategoria(ProfissionalSaude.Categoria.MEDICO);

        MvcResult result = mockMvc.perform(post("/api/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Dr. Augusto Lima"))
                .andReturn();

        Long id = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("id").asLong();

        // 2. BUSCAR o profissional criado pelo ID
        mockMvc.perform(get("/api/profissionais/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoria").value("MEDICO"));

        // 3. ALTERAR dados do profissional
        p.setNome("Dr. Augusto Lima Silva");
        mockMvc.perform(put("/api/profissionais/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Dr. Augusto Lima Silva"));

        // 4. EXCLUIR profissional
        mockMvc.perform(delete("/api/profissionais/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void deveVincularAtendimentoEExameAoProfissional() throws Exception {
        // 1. Criar o Profissional de Saúde base
        ProfissionalSaude p = new ProfissionalSaude();
        p.setNome("Dra. Beatriz Costa");
        p.setCategoria(ProfissionalSaude.Categoria.PSICOLOGO);

        MvcResult pResult = mockMvc.perform(post("/api/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andReturn();

        Long profissionalId = objectMapper.readTree(pResult.getResponse().getContentAsString())
                .get("id").asLong();

        // 2. Criar Atendimento vinculado a esse profissional
        String atendimentoJson = String.format("""
            {
                "titulo": "Sessão Terapia Semanal",
                "data": "2026-07-15",
                "horario": "15:00:00",
                "profissional": {"id": %d}
            }
            """, profissionalId);

        MvcResult aResult = mockMvc.perform(post("/api/atendimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(atendimentoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Sessão Terapia Semanal"))
                .andReturn();

        Long atendimentoId = objectMapper.readTree(aResult.getResponse().getContentAsString())
                .get("id").asLong();

        // 3. Criar Exame de Laboratório vinculado ao atendimento gerado acima
        String exameJson = String.format("""
            {
                "descricao": "Avaliação Cognitiva Computacional",
                "posologia": "Trazer óculos de grau se utilizar",
                "atendimento": {"id": %d}
            }
            """, atendimentoId);

        mockMvc.perform(post("/api/exames")
                .contentType(MediaType.APPLICATION_JSON)
                .content(exameJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Avaliação Cognitiva Computacional"));
    }
}