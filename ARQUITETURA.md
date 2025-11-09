# Arquitetura — Patio Vision (DevOps Tools & Cloud Computing)

## 1) Visão Geral (Componentes e Conexões)

```mermaid
flowchart LR

  USER((Usuario Final))
  DEV((Desenvolvedor))

  subgraph GitHub["GitHub"]
    REPO[Repositorio\npatio-vision-api]
  end

  subgraph DevOps["Azure DevOps (CI/CD)"]
    CI[1) Pipeline CI\nBuild & Push Docker]
    CD[2) Pipeline CD\nDeploy ACI + Web App]
  end

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

  subgraph APPSTACK["Aplicacao Patio Vision"]
    FE[Thymeleaf + TailwindCSS + DaisyUI]
    BE[Spring Boot API\nPorta 8080]
  end

  USER -->|HTTP/HTTPS| APPSTACK
  APPSTACK --> FE
  APPSTACK --> BE
  BE -->|JDBC + SSL| DB
  APPSTACK -->|OAuth2 Login| OAUTH
  DEV -->|Push codigo| REPO
  REPO --> CI
  CI -->|Build Docker\nPush image| ACR
  CD -->|Puxa imagem do ACR| ACI
  CD -->|Atualiza container do\nWeb App| APP
  PLAN -. hospeda .- APP
  ACI -->|API 8080| USER
```
