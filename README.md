# Agenda Web - Profissionais de Saúde

Sistema de Agenda Web para gerenciamento de Profissionais de Saúde, Atendimentos e Exames de Laboratório.

## Links do Projeto em Produção

* **Link da Aplicação (Frontend):** https://agenda-frontend-he2x.onrender.com
* **API REST (Backend):** https://agenda-web-8gfo.onrender.com/api
* **Documentação Swagger:** https://agenda-web-8gfo.onrender.com/swagger-ui.html

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

Para rodar o ambiente completo de desenvolvimento local com persistência de dados, utilize o Docker Compose:

```bash
# Iniciar os containers em segundo plano
docker-compose up -d

# URLs Locais:
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
├── render.yaml        # Configuração de Infraestrutura como Código (IaC)
└── .github/workflows/ci-cd.yml
```

## Arquitetura de Deploy no Render.com

Para a publicação final da aplicação na nuvem através do Render, optou-se por uma estratégia de **Deploy Manual de Recursos Isolados**, uma vez que a plataforma exige verificação de conta com cartão de crédito para a execução de automações via *Blueprints Orchestrator*.

A infraestrutura foi dividida e conectada da seguinte forma:

1. **Banco de Dados (PostgreSQL):** Criado uma instância nativa do Postgres 15 (`agenda-db`) utilizando o plano gratuito da plataforma.
2. **Backend (Web Service Docker):** Criado a partir do código contido na subpasta `/backend`. Foi configurado o contexto de build para a pasta correspondente e injetadas as variáveis de ambiente (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`) apontando dinamicamente para as credenciais do banco de dados PostgreSQL criado.
3. **Frontend (Static Site):** Publicado como um site estático focado na subpasta `/frontend`. O comando de build executado foi o `npm run build` apontando para o diretório de publicação `build`. Foi injetada a variável de ambiente `REACT_APP_API_URL` apontando para a URL ativa do Web Service do backend em produção.

### Utilização Futura do Blueprint (`render.yaml`)
Apesar do deploy atual ter sido realizado através do gerenciamento individual de recursos na interface do painel, o arquivo de Infraestrutura como Código `render.yaml` encontra-se totalmente atualizado e funcional na raiz do projeto. Usuários com contas verificadas na plataforma podem realizar o deploy automatizado de toda a stack clicando em **New > Blueprint** e conectando este repositório.
