# Arquitetura — Patio Vision (DevOps Tools & Cloud Computing)

## 1) Visão Geral (Componentes e Conexões)

```mermaid
flowchart LR

  %% Usuários
  USER((Usuário Final))
  DEV((Desenvolvedor))

  %% GitHub
  subgraph GitHub["GitHub"]
    REPO[Repositório: patio-vision-api]
  end

  %% DevOps
  subgraph DevOps["Azure DevOps (CI/CD)"]
    CI[1) Pipeline CI — Build e Push Docker]
    CD[2) Pipeline CD — Deploy ACI + Web App]
  end

  %% Azure Cloud
  subgraph Azure["Azure Cloud"]
    subgraph RG["Resource Group: rg-patiovision"]
      
      ACR[(Azure Container Registry — acr558090)]
      ACI[(Azure Container Instances — aci558090)]

      subgraph AS["App Service"]
        PLAN["App Service Plan — planACRWebApp (Linux F1)"]
        APP["Web App — acrwebapp558090"]
      end

      DB[(PostgreSQL Flexible Server — patio_vision)]
      OAUTH[(OAuth2 Providers — Google / GitHub)]

    end
  end

  %% App Stack
  subgraph APPSTACK["Aplicação Patio Vision"]
    FE[Frontend — Thymeleaf + TailwindCSS + DaisyUI]
    BE[Backend — Spring Boot API (porta 8080)]
  end

  %% Fluxos
  USER -->|HTTP/HTTPS| APPSTACK
  APPSTACK --> FE
  APPSTACK --> BE
  BE -->|JDBC + SSL| DB
  APPSTACK -->|OAuth2 Login| OAUTH
  DEV -->|Push de código| REPO
  REPO --> CI
  CI -->|Build Docker e Push da imagem| ACR
  CD -->|Puxa imagem do ACR| ACI
  CD -->|Atualiza container do Web App| APP
  PLAN -. hospeda .- APP
  ACI -->|API porta 8080| USER
