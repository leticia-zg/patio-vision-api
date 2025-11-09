# Arquitetura — Patio Vision (DevOps Tools & Cloud Computing)

## 1) Visão Geral (Componentes e Conexões)

```mermaid
flowchart LR

  USER((Usuario Final))
  DEV((Desenvolvedor))

  %% GITHUB
  subgraph GitHub["GitHub"]
    REPO[Repositorio<br/>patio-vision-api]
  end

  %% DEVOPS
  subgraph DevOps["Azure DevOps (CI/CD)"]
    CI[1) Pipeline CI<br/>Build & Push Docker]
    CD[2) Pipeline CD<br/>Deploy ACI + Web App]
  end

  %% AZURE CLOUD
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

  %% APP STACK
  subgraph APPSTACK["Aplicacao Patio Vision"]
    FE[Thymeleaf + TailwindCSS + DaisyUI]
    BE[Spring Boot API<br/>Porta 8080]
  end

  %% FLUXOS
  USER -->|HTTP/HTTPS| APPSTACK

  APPSTACK --> FE
  APPSTACK --> BE

  BE -->|JDBC + SSL| DB

  APPSTACK -->|OAuth2 Login| OAUTH

  DEV -->|Push codigo| REPO
  REPO --> CI

  CI -->|Build Docker<br/>Push image| ACR

  CD -->|Puxa imagem do ACR| ACI
  CD -->|Atualiza container do<br/>Web App| APP

  PLAN -. hospeda .- APP

  ACI -->|API 8080| USER

```
