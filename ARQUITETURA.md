# Arquitetura — Patio Vision (DevOps Tools & Cloud Computing)

## Visão Geral (Componentes e Conexões)

```mermaid
flowchart LR

USER((Usuario Final))
DEV((Desenvolvedor))

subgraph GitHub["GitHub"]
  REPO[Repository: leticia-zg/patio-vision-api]
  ACTIONS[GitHub Actions]
end

subgraph DevOps["Azure DevOps CI/CD"]
  CI[1. CI - Build Maven + JUnit + Docker Image]
  CD[2. CD - Release / Deploy Container]
end

subgraph Azure["Azure Cloud - rg-patiovision"]
  ACR[(ACR - acrrm558090)]
  ACI[(ACI - acirm558090)]
  WEBAPP[(Web App - acrwebapprm558090)]
  PLAN[(App Service Plan - Linux F1)]
  DB[(PostgreSQL Flexible Server - futurestack)]
  AI[(Application Insights)]
end

%% --- FLUXO DEVOPS ---
DEV -->|1. Push codigo| REPO
REPO -->|2. Dispara workflow| ACTIONS
ACTIONS -->|3. CI Build| CI
CI -->|4. Push Docker Image| ACR
CD -->|5. Deploy Container| ACI
CD -->|6. Deploy Container| WEBAPP

ACR <--> ACI

%% --- EXECUCAO USUARIO ---
USER -->|HTTP/HTTPS| WEBAPP
WEBAPP -->|JDBC SSL| DB
WEBAPP -->|Logs/Metricas| AI

PLAN -. hospeda .- WEBAPP

```
- PostgreSQL Flexible Server: `pg-rm1556219` (DB `patio_vision`)
- Application Insights: `ai-pg-rm556219`
