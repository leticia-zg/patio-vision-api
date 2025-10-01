# Arquitetura da Solução — Patio Vision (App Service + PostgreSQL + GitHub Actions)

> **Recursos e nomes reais do ambiente**
> - **Web App**: `app-pg-rm556219`
> - **App Service Plan**: `plan-pg-rm556219`
> - **Banco de Dados (PostgreSQL Flexible Server)**: `pg-rm1556219`
> - **Application Insights**: `ai-pg-rm1556219` (nome esperado conforme criação original)
> - **Resource Group**: `rg-patio-vision`
> - **Região**: `brazilsouth`

---

## 1) Visão Geral (Componentes e Conexões)

```mermaid
flowchart LR
    subgraph GitHub["GitHub"]
      DEV[(Developer)] -- "git push (main)" --> REPO[(Repositório
leticia-zg/patio-vision-api)]
      REPO -->|CI/CD Workflow| GHA[GitHub Actions
Build & Deploy]
    end

    subgraph Azure["Microsoft Azure"]
      subgraph RG["Resource Group: rg-patio-vision"]
        PLAN[(App Service Plan
plan-pg-rm556219
Linux F1)]
        APP[["Web App
app-pg-rm556219
Java 17 (JAR)"]]
        DB[(Azure Database for PostgreSQL
Flexible Server
pg-rm1556219)]
        AI[(Application Insights
ai-pg-rm1556219)]
      end
    end

    USER([Usuário Web]) -->|HTTP/HTTPS| APP
    GHA -->|Deploy via Publish Profile| APP
    APP -->|JDBC (SSL)| DB
    APP -->|Telemetria/Logs| AI
```

**Recursos principais**  
- **App Service Plan (Linux F1)**: hospedagem PaaS do Web App.
- **Web App (Java 17)**: executa o JAR empacotado pelo Maven (Spring Boot MVC/Thymeleaf).
- **PostgreSQL Flexible Server**: base de dados `patio_vision` no servidor **pg-rm1556219**, com acesso SSL obrigatório.
- **Application Insights**: telemetria, métricas e live logs.
- **GitHub Actions**: pipeline CI/CD para build & deploy contínuo.

---

## 2) Pipeline (CI/CD)

```mermaid
sequenceDiagram
    participant Dev as Dev
    participant Repo as GitHub Repo
    participant GHA as GitHub Actions
    participant App as Azure Web App (app-pg-rm556219)
    participant DB as Azure PostgreSQL (pg-rm1556219)
    participant AI as App Insights (ai-pg-rm1556219)

    Dev->>Repo: git push (branch main)
    Repo->>GHA: Dispara workflow (deploy-azure.yml)
    GHA->>GHA: Maven build (clean package, JDK 17)
    GHA->>App: Deploy do JAR (azure/webapps-deploy@v2)
Publish Profile (Secret)
    App->>DB: Conexão JDBC (SSL),
 aplica Flyway (migrações V1..V6)
    App->>AI: Telemetria/Logs (agent codeless)
    Dev->>App: Acessa URL pública
(app-pg-rm556219.azurewebsites.net)
```

**Segredos & Configuração**
- `AZURE_WEBAPP_PUBLISH_PROFILE` (secret) – credencial para o deploy.
- Variáveis de runtime (conexão com o Postgres) ficam em **App Settings** do Web App (via CLI):
  - `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
  - `SPRING_PROFILES_ACTIVE=prod`, `WEBSITES_PORT=8080`
  - `APPLICATIONINSIGHTS_CONNECTION_STRING`, etc.

---

## 3) Execução em Produção (Runtime)

1. O **Web App** inicia o Spring Boot (porta 80 no container; `WEBSITES_PORT=8080` para compatibilidade).
2. O Spring Boot **aplica migrações Flyway** contra o **PostgreSQL** (**pg-rm1556219** / schema `public`), garantindo que a estrutura esteja atualizada.
3. A aplicação expõe as rotas MVC/Thymeleaf e APIs (se houver).
4. **Application Insights** coleta métricas/telemetria automaticamente (agent codeless).

**Conexão ao Banco (exemplo):**
```
jdbc:postgresql://pg-rm1556219.postgres.database.azure.com:5432/patio_vision?sslmode=require
user=pgadmin
password=********
```

---

## 4) Detalhamento de Recursos (comandos de criação)

- **Resource Group**: agrupador lógico na região `brazilsouth`.
- **PostgreSQL Flexible Server**: `Standard_B1ms`, 32GB, versão 16, acesso público (range liberado).
- **App Insights**: recurso de observabilidade (connection string usada como App Setting).
- **App Service Plan (Linux F1)**: camada grátis para testes.
- **Web App (Java 17)**: runtime gerenciado pela Azure, recebe JAR pelo GitHub Actions.

> Todos estes recursos foram provisionados via **Azure CLI** (script no seu guia).

---

## 5) Operação & Observabilidade

- **Logs em tempo real**: `az webapp log tail -g rg-patio-vision -n app-pg-rm556219`
- **Reinício da app**: `az webapp restart -g rg-patio-vision -n app-pg-rm556219`
- **Ver App Settings**: `az webapp config appsettings list -g rg-patio-vision -n app-pg-rm556219 -o table`
- **Application Insights**: analisar requisições, dependências e exceptions.

---

## 6) Segurança & Boas Práticas Rápidas

- Manter cadeias JDBC e senhas **fora do repositório** (App Settings / Secrets).
- Habilitar **SSL obrigatório** para conexão com o Postgres (`sslmode=require`).
- Usar **roles e permissões** específicas do DB para a aplicação (evitar admin em produção).
- Em produção, considerar camadas pagas do App Service e Postgres para **SLAs** e **desempenho**.

---

## 7) Endereço Público

- **URL**: `https://app-pg-rm556219.azurewebsites.net`
> - Veja a documentação e diagramas: [docs/ARQUITETURA.md](./docs/ARQUITETURA.md)
> ```
