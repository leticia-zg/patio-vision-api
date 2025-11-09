# Arquitetura — Patio Vision (DevOps Tools & Cloud Computing)

## 1) Visão Geral (Componentes e Conexões)

```mermaid
flowchart LR
    %% === PERSONAS ===
    subgraph Personas
        U[Usuário Final<br/>(Operador/Admin)]
        D[Desenvolvedor]
    end

    %% === DEVOPS PIPELINE ===
    subgraph CI[1) CI - Integração Contínua]
        A1[1. Commit no GitHub]
        A2[2. Pipeline CI acionada]
        A3[3. Build Docker<br/>(Gradle + Dockerfile)]
        A4[4. Push para Azure Container Registry<br/>(ACR)]
    end

    subgraph CD[2) CD - Entrega Contínua]
        B1[5. Pipeline CD acionada]
        B2[6. Azure CLI autentica no Azure]
        B3[7. Atualiza ACI<br/>(Container Instances)]
        B4[8. Atualiza App Service<br/>(Web App)]
    end

    %% === CLOUD ARCHITECTURE ===
    subgraph Cloud[Azure Cloud]
        ACR[(Azure Container Registry)]
        ACI[(Azure Container Instances)]
        APP[(Azure App Service<br/>Web App)]
        DB[(Azure PostgreSQL<br/>Flexible Server)]
        OAUTH[(OAuth2<br/>GitHub / Google)]
    end

    %% === APPLICATION COMPONENTS ===
    subgraph App[Aplicação Patio Vision]
        FE[Frontend<br/>Thymeleaf + Tailwind + DaisyUI]
        BE[Backend<br/>Spring Boot 3]
    end

    %% ======= RELAÇÕES PERSONAS -> SISTEMA =======
    U -->|Acessa via browser| APP
    APP --> FE
    APP --> BE

    U -->|Autenticação| OAUTH
    BE --> DB

    %% ======= DEVOPS -> ACR =======
    D -->|Push/Commit| A1
    A1 --> A2 --> A3 --> A4 --> ACR

    %% ======= CD -> DEPLOY =======
    ACR --> B1 --> B2 --> B3
    ACR --> B4

    %% ======= DEPLOY EXECUTION =======
    B3 --> ACI
    B4 --> APP

    %% ======= APP RUNTIME =======
    ACI -->|Exposição da API 8080| U
    APP -->|Interface Web| U
```
