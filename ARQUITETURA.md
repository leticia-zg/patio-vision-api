# Arquitetura — Patio Vision (DevOps Tools & Cloud Computing)

## 1) Visão Geral (Componentes e Conexões)

```mermaid
flowchart LR
  %% ===== GITHUB =====
  subgraph GitHub["GitHub"]
    DEV[Developer] -- "git push (main)" --> REPO["Repositório<br/>leticia-zg/patio-vision-api"]
    REPO -->|CI/CD Workflow| GHA["GitHub Actions<br/>Build & Deploy"]
  end

  %% ===== AZURE =====
  subgraph Azure["Microsoft Azure"]
    subgraph RG["Resource Group: rg-patio-vision"]
      PLAN["App Service Plan<br/>plan-pg-rm556219<br/>(Linux F1)"]
      APP["Web App<br/>app-pg-rm556219"]
      DB["PostgreSQL Flexible Server<br/>pg-rm1556219<br/>DB: patio_vision"]
      AI["Application Insights<br/>ai-pg-rm1556219"]
    end
  end

  %% ===== RELAÇÕES =====
  PLAN --> APP
  GHA -->|Publish Profile / ZIP (JAR)| APP
  USER[Usuário] -->|HTTPS 443| APP
  APP -->|JDBC (SSL/TLS 5432)| DB
  APP -->|Telemetry| AI
```

## 2) Fluxo de Deploy (CI/CD — GitHub Actions)

1. **Developer** faz `git push` na branch **main** do repositório.
2. O **GitHub Actions** é disparado pelo workflow (`azure-webapps.yml`).  
   - **Build**: compila o projeto Java (Maven) e gera o `*.jar` em `target/`.
   - **Deploy**: usa a action `azure/webapps-deploy` com o **Publish Profile** do Web App para publicar o artefato no Azure App Service.
3. O **App Service (app-pg-rm556219)** inicia o container Java 17 e sobe a aplicação.

## 3) Execução em Produção (PaaS)

- **Plano (plan-pg-rm556219)**: camada Linux **F1 (Free)**.
- **Web App** serve a aplicação Spring Boot na porta interna 80 (App Service faz o binding automaticamente).
- **Banco**: **PostgreSQL Flexible Server `pg-rm1556219`**, database `patio_vision`.
- **Conexão**: string JDBC com `sslmode=require` (TLS) via 5432.
- **Telemetria**: **Application Insights `ai-pg-rm1556219`** (variável `APPLICATIONINSIGHTS_CONNECTION_STRING`).

## 4) Configuração por Variáveis (App Settings do Web App)

As chaves principais (definidas via Azure CLI) são:

- `SPRING_DATASOURCE_URL=jdbc:postgresql://pg-rm1556219.postgres.database.azure.com:5432/patio_vision?sslmode=require`  
- `SPRING_DATASOURCE_USERNAME=pgadmin`  
- `SPRING_DATASOURCE_PASSWORD=***`  
- `SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect`  
- `SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver`  
- `APPLICATIONINSIGHTS_CONNECTION_STRING=InstrumentationKey=...`  
- (opcional) `SPRING_PROFILES_ACTIVE=prod`

Essas variáveis **não** ficam no repositório — são gerenciadas no **App Service**.

## 5) Segurança e Acesso

- Tráfego ao app via **HTTPS 443**: `https://app-pg-rm556219.azurewebsites.net`  
- Tráfego App → DB via **JDBC (TLS)**: porta **5432**.
- Publicação apenas via **Publish Profile** (credencial secreta) armazenada em **GitHub Secrets**.

## 6) Recursos Criados (Azure)

- **Resource Group**: `rg-patio-vision`  
- **App Service Plan**: `plan-pg-rm556219` (Linux F1)  
- **Web App**: `app-pg-rm556219`  
- **PostgreSQL Flexible Server**: `pg-rm1556219` + **DB** `patio_vision`  
- **Application Insights**: `ai-pg-rm1556219`
