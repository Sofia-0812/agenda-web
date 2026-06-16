# Agenda Web - Profissionais de Saúde

Sistema de Agenda Web para gerenciamento de Profissionais de Saúde, Atendimentos e Exames de Laboratório.

## Tecnologias

| Camada | Tecnologia |
|--------|-----------|
| Backend | Java 17 + Spring Boot 3.2 |
| Frontend | React 18 + React Router |
| Banco de Dados | PostgreSQL 15 |
| Build Backend | Maven |
| Build Frontend | Node.js 20 + npm |
| Versionamento | Git + GitHub |
| CI/CD | GitHub Actions |
| Containers | Docker + Docker Compose |
| Produção | Render.com |

## Entidades

- **ProfissionalSaude** — id, nome, telefone, endereço, categoria (Médico / Fisioterapeuta / Psicólogo)
- **Atendimento** — id, data, horário, título, link_video_conferencia, receita, profissional (FK)
- **ExameLaboratorio** — id, descrição, posologia, atendimento (FK)

## Métodos da API

| Entidade | Endpoint | Operações |
|----------|----------|-----------|
| Profissional | `/api/profissionais` | Inserir, Alterar(id), Consultar(nome), Consultar(categoria), Consultar(id), Excluir(id) |
| Atendimento | `/api/atendimentos` | CRUD completo |
| Exame | `/api/exames` | CRUD completo |

## Executar Localmente

```bash
# Usando Docker Compose
docker-compose up -d

# Backend: http://localhost:8080
# Frontend: http://localhost:3000
# Swagger: http://localhost:8080/swagger-ui.html
```

## Estrutura do Projeto

```
agenda-web/
├── backend/           # API REST (Java/Spring Boot)
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/agenda/
│       ├── model/     # ProfissionalSaude, Atendimento, ExameLaboratorio
│       ├── repository/
│       └── controller/
├── frontend/          # UI (React 18)
│   ├── package.json
│   ├── Dockerfile
│   └── src/components/
├── docker-compose.yml
├── render.yaml        # Deploy no Render
└── .github/workflows/ci-cd.yml
```

## Deploy no Render

1. Faça fork/clone deste repositório para sua conta GitHub
2. Acesse [render.com](https://render.com) → New → Blueprint
3. Conecte ao repositório
4. O `render.yaml` configura automaticamente backend + frontend + banco
