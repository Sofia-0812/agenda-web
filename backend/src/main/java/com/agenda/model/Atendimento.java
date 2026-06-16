package com.agenda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "atendimento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Data é obrigatória")
    private LocalDate data;

    private LocalTime horario;

    @NotBlank(message = "Título é obrigatório")
    @Column(length = 200, nullable = false)
    private String titulo;

    @Column(name = "link_video_conferencia", length = 500)
    private String linkVideoConferencia;

    @Enumerated(EnumType.STRING)
    private TipoReceita receita;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profissional_id")
    private ProfissionalSaude profissional;

    public enum TipoReceita {
        REMEDIO, ATIVIDADE_FISICA, ATIVIDADE_MENTAL
    }
}
