# Arquitetura — Patio Vision (DevOps Tools & Cloud Computing)

## 1) Visão Geral (Componentes e Conexões)

```mermaid
flowchart LR

  %% ========= PERSONAS =========
  USER((Usuário Final))
  DEV((Desenvolvedor))

  %% ========= GITHUB & DEVOPS =========
  subgraph GitHub["GitHub"]
    REPO[Repositório\npatio-vision-api]
  end

  subgraph DevOps["Azure DevOps (CI/CD)"]
    CI[1) Pipeline CI\nBuild & Push Docker]
    CD[2) Pipeline CD\nDeploy ACI + Web App]
  end

  %% ========= AZURE CLOUD =========
  subgraph Azure["Azure Cloud"]
    
    subgraph RG["Resource Group\nrg-patiovision"]
      
      ACR[(Azure Container Registry\nacr558090)]
      ACI[(Azure Container Instances\naci558090)]
      
      subgraph AS["App Service"]
        PLAN["App Service Plan\nplanACRWebApp (Linux F1)"]
        APP["Web App\nacrwebapp558090"]
      end

      DB[(PostgreSQL Flexible Server\npatio_vision)]
      OAUTH[(OAuth2 Providers\nGoogle/GitHub)]
    end
  end

  %% ========= FRONTEND / BACKEND =========
  subgraph APPSTACK["Aplicação Patio Vision"]
    FE[Thymeleaf + TailwindCSS + DaisyUI]
    BE[Spring Boot API\nPorta 8080]
  end

  %% ========= FLUXOS =========
  USER -->|HTTP/HTTPS| APP
  APP --> FE
  APP --> BE
  BE -->|JDBC + SSL| DB
  APP -->|OAuth2 Login| OAUTH
  DEV -->|Push código| REPO
  REPO --> CI
  CI -->|Build Docker\nPush image| ACR
  CD -->|Puxa imagem do ACR| ACI
  CD -->|Atualiza container do\nWeb App| APP
  PLAN -. hospeda .- APP
  ACI -->|API 8080| USER

```
