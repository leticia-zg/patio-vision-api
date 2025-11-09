# Arquitetura — Patio Vision (DevOps Tools & Cloud Computing)

## 1) Visão Geral (Componentes e Conexões)

```mermaid
flowchart LR

  %% ========= PERSONAS =========
  USER((Usuário Final))
  DEV((Desenvolvedor))

  %% ========= GITHUB & DEVOPS =========
  subgraph GitHub["GitHub"]
    REPO[Repositório<br/>patio-vision-api]
  end

  subgraph DevOps["Azure DevOps (CI/CD)"]
    CI[1) Pipeline CI<br/>Build & Push Docker]
    CD[2) Pipeline CD<br/>Deploy ACI + Web App]
  end

  %% ========= AZURE CLOUD =========
  subgraph Azure["Azure Cloud"]
    
    subgraph RG["Resource Group<br/>rg-patiovision"]
      
      ACR[(Azure Container Registry<br/>acr558090)]
      ACI[(Azure Container Instances<br/>aci558090)]
      
      subgraph AS["App Service"]
        PLAN["App Service Plan<br/>planACRWebApp (Linux F1)"]
        APP["Web App<br/>acrwebapp558090"]
      end

      DB[(PostgreSQL Flexible Server<br/>patio_vision)]
      OAUTH[(OAuth2 Providers<br/>Google/GitHub)]
    end
  end

  %% ========= FRONTEND / BACKEND =========
  subgraph APPSTACK["Aplicação Patio Vision"]
    FE[Thymeleaf + TailwindCSS + DaisyUI]
    BE[Spring Boot API<br/>Porta 8080]
  end

  %% ========= FLUXOS =========

  %% -- Personas -> App
  USER -->|HTTP/HTTPS| APP

  %% -- App decomposição
  APP --> FE
  APP --> BE

  %% -- Backend -> Banco
  BE -->|JDBC + SSL| DB

  %% -- Login OAuth
  APP -->|OAuth2 Login| OAUTH

  %% -- Dev workflow
  DEV -->|Push código| REPO
  REPO --> CI

  %% -- CI -> ACR
  CI -->|Build Docker<br/>Push image| ACR

  %% -- CD -> Infra
  CD -->|Puxa imagem do ACR| ACI
  CD -->|Atualiza container do<br/>Web App| APP

  %% -- APP Service Hosting
  PLAN -. hospeda .- APP

  %% -- ACI expõe a API
  ACI -->|API 8080| USER


```
