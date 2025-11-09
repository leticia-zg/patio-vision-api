### Atualização para teste da pipeline CI/CD

# 🏍️ Patio Vision - Sistema de Gerenciamento de Pátios

Sistema web e mobile para gerenciamento inteligente de pátios de motos, permitindo controle de ocupação, entrada e saída de veículos através de dispositivos IoT.

## 👥 Autoras

- **Ana Carolina Reis Santana** - RM556219
- **Letícia Zago de Souza** - RM558464  
- **Celina Alcântara do Carmo** - RM558090

## 📖 Sobre o Projeto

O **Patio Vision** é um sistema completo de gerenciamento de pátios de motos que oferece:

### 📱 **Interface Mobile**
- Registro de entrada e saída de motos via aplicativo mobile
- Controle por operadores em campo
- Identificação via dispositivos IoT das motos

### 🖥️ **Interface Web**
- Monitoramento em tempo real da ocupação dos pátios
- Dashboard com indicadores de capacidade e ocupação
- Gerenciamento completo de pátios, setores e motos
- Sistema de autenticação híbrido (OAuth2 + formulário)

### 🔧 **Funcionalidades Principais**

1. **Gestão de Pátios**
   - Cadastro e edição de pátios
   - Monitoramento de capacidade total

2. **Gestão de Setores**
   - Organização dos pátios em setores
   - Controle de capacidade máxima por setor
   - Visualização de ocupação atual

3. **Gestão de Motos**
   - Registro com identificador IoT único
   - Controle de entrada e saída
   - Histórico de permanência

4. **Dashboard Inteligente**
   - Indicadores visuais de ocupação
   - Filtros por pátio específico
   - Estatísticas em tempo real

5. **Sistema de Autenticação**
   - Login via GitHub e Google (OAuth2)
   - Login tradicional com usuário e senha
   - Registro de novos usuários

## 💼 Benefícios para o Negócio
- **Visibilidade em tempo real** da ocupação (evita superlotação e melhora o uso do espaço).
- **Agilidade operacional** no registro de entrada/saída, reduzindo filas e erros manuais.
- **Rastreabilidade e auditoria** (histórico por moto/setor/pátio).
- **Redução de perdas e retrabalhos** com dados padronizados e integrações IoT.
- **Base para decisões** (indicadores e métricas) e **escalabilidade** na nuvem.

### 📌 Stack Utilizada

- **Backend:** Spring Boot 3, Spring Security, Spring Data JPA  
- **Frontend:** Thymeleaf, TailwindCSS, DaisyUI  
- **Banco de Dados:** PostgreSQL + Flyway  
- **Infraestrutura (Cloud):** Docker, Azure Container Registry, Azure Container Instances, Azure App Service  
- **Autenticação:** OAuth2 (Google e GitHub)  
- **DevOps:** GitHub + Azure DevOps (Pipelines CI/CD)

## 🚀 Infra Azure

- Script para criação da infra:  
  ➜ [Infra.md](./Infra.md)

## 🧪 Testes (scripts)

- Scripts de testes da API (curl/Postman/HTTP Client):  
  ➜ [scripts/tests/](./src/script_db.sql)

## 🛠️ Arquitetura da solução
  
  ➜ [ARQUITETURA.md](./ARQUITETURA.md)
  
## 🔧 Detalhamento dos Componentes

| **Nome do Componente** | **Tipo** | **Descrição Funcional** | **Tecnologia / Ferramenta** |
|------------------------|----------|--------------------------|-----------------------------|
| Repositório de Código | SCM (Source Code Management) | Armazena e versiona todo o código-fonte do projeto, incluindo backend, frontend, Dockerfile e documentação. | GitHub |
| Pipeline de CI | Integração Contínua | Constrói a aplicação, gera a imagem Docker e envia ao Azure Container Registry (ACR). Executada automaticamente a cada push na branch `main`. | Azure DevOps Pipelines (YAML) |
| Pipeline de CD | Entrega Contínua | Realiza o deploy da imagem no Azure Container Instances (ACI) e atualiza o Azure App Service com a nova build. | Azure DevOps Pipelines (YAML) |
| Azure Container Registry (ACR) | Registry de Containers | Armazena todas as imagens Docker versionadas geradas pela pipeline de CI. | Azure ACR |
| Azure Container Instances (ACI) | Execução de Containers | Roda a aplicação Spring Boot em ambiente serverless, permitindo deploy rápido. | Azure Container Instances |
| Azure App Service (Web App) | Hospedagem Web | Hospeda a interface web e backend (container Docker) da aplicação. | Azure App Service (Linux Plan F1) |
| App Service Plan | Camada de Computação | Fornece recursos (CPU, memória) para o Web App. | Azure App Service Plan |
| Banco PostgreSQL | Banco de Dados (DBaaS) | Armazena pátios, setores, motos, usuários e histórico de movimentação. | Azure Database for PostgreSQL – Flexible Server |
| Flyway | Versionamento de Banco | Gerencia migrations SQL executadas durante o bootstrap da aplicação. | Flyway |
| Backend Spring Boot | API e Lógica de Negócio | Processa requisições, regras de negócio, acesso ao banco e autenticação. | Java 17 + Spring Boot 3.5.4 |
| Frontend Web | Interface do Usuário | Interface responsiva baseada em servidor com Thymeleaf. | Thymeleaf + TailwindCSS + DaisyUI |
| OAuth2 Login | Autenticação | Permite autenticação via Google e GitHub. | Spring Security OAuth2, Google OAuth, GitHub OAuth |
| Dockerfile | Empacotamento | Define como o container da aplicação é construído via Gradle e JRE 17. | Docker |
| Agente de Build | Executor do Pipeline | Ambiente onde CI/CD é executado (ubuntu-latest). | Azure DevOps Hosted Agent |
| Azure CLI | Automação de Deploy | Utilizada pela pipeline CD para criar/atualizar ACI e App Service. | Azure CLI |
| DNS Público do ACI | Endereço Público | URL pública exposta pelo container da API. Ex: `aci558090.eastus.azurecontainer.io:8080` | ACI DNS |
| Usuário Final | Persona | Acessa a interface web/mobile para registrar motos e monitorar pátios. | — |
| Desenvolvedor | Persona | Realiza commits, revisões e aciona pipelines de CI/CD. | — |

## 🚀 Como Executar o Projeto

### Pré-requisitos
- Java 17+
- Docker e Docker Compose
- Gradle

### 1. Clone o repositório
```bash
git clone [https://github.com/AnaCarolSant/patio-vision-api.git](https://github.com/leticia-zg/patio-vision-api.git)
cd patio-vision-api
```

### 2. Configure as variáveis de ambiente (Para OAuth2)

Para utilizar login via GitHub e Google, você precisa configurar as seguintes variáveis de ambiente:

#### GitHub OAuth2
1. Acesse: https://github.com/settings/developers
2. Crie uma nova OAuth App
3. Configure as URLs:
   - Homepage URL: `http://localhost:8080`
   - Authorization callback URL: `http://localhost:8080/login/oauth2/code/github`

#### Google OAuth2
1. Acesse: https://console.cloud.google.com/
2. Crie um projeto ou selecione um existente
3. Ative a Google+ API
4. Configure OAuth 2.0:
   - Authorized redirect URIs: `http://localhost:8080/login/oauth2/code/google`

#### Arquivo .env (criar na raiz do projeto)
```properties
# GitHub OAuth2
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GITHUB_CLIENT_ID=seu_github_client_id
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GITHUB_CLIENT_SECRET=seu_github_client_secret

# Google OAuth2
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID=seu_google_client_id
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET=seu_google_client_secret
```

### 3. Execute o banco de dados
```bash
docker-compose up postgres -d
```

### 4. Execute a aplicação
```bash
./gradlew bootRun
```

A aplicação estará disponível em: http://localhost:8080

## 🚀 Executando em Produção (Azure)

A aplicação também está configurada para rodar em ambiente cloud usando:

- Azure Container Registry (ACR)
- Azure Container Instances (ACI)
- Azure App Service

### 🔹 Deploy automático (CI/CD)
O GitHub e o Azure DevOps realizam:

1. **Build da imagem Docker**
2. **Push para o ACR**
3. **Deploy no ACI e App Service**

### 🔹 Endpoints em produção
- API (ACI): `http://acirm558090.eastus.azurecontainer.io:8080`
- Web App (App Service): `https://acrwebapprm558090.azurewebsites.net`

### 🔹 Scripts de infraestrutura
Os scripts utilizados para criar ACR, banco PostgreSQL e ACI/App Service estão na pasta:
  ➜ [Infra.md](./Infra.md)

## 📡 API Endpoints

### 🔐 Autenticação

#### Login via Formulário
```http
POST /auth/login
Content-Type: application/x-www-form-urlencoded

username=usuario@email.com&password=senha123
```

#### Registro de Usuário
```http
POST /auth/register
Content-Type: application/x-www-form-urlencoded

name=Nome Usuario&email=usuario@email.com&password=senha123&confirmPassword=senha123
```

#### Login OAuth2
```http
GET /oauth2/authorization/github
GET /oauth2/authorization/google
```

#### Logout
```http
POST /logout
```

### 🏢 Pátios

#### Listar Pátios
```http
GET /patio
Accept: application/json
```

#### Criar Pátio
```http
POST /patio/form
Content-Type: application/x-www-form-urlencoded

nome=Patio Central
```

#### Atualizar Pátio
```http
PUT /patio/{id}
Content-Type: application/x-www-form-urlencoded

nome=Patio Central Atualizado
```

#### Excluir Pátio
```http
DELETE /patio/{id}
```

### 🏭 Setores

#### Listar Setores
```http
GET /setor
Accept: application/json
```

#### Filtrar Setores por Pátio
```http
GET /setor?patioId=1
Accept: application/json
```

#### Criar Setor
```http
POST /setor/form
Content-Type: application/x-www-form-urlencoded

nome=Setor A&capacidadeMaxima=50&patioId=1
```

#### Atualizar Setor
```http
PUT /setor/{id}
Content-Type: application/x-www-form-urlencoded

nome=Setor A Atualizado&capacidadeMaxima=60&patioId=1
```

#### Excluir Setor
```http
DELETE /setor/{id}
```

### 🏍️ Motos

#### Listar Motos
```http
GET /moto
Accept: application/json
```

#### Registrar Entrada de Moto
```http
POST /moto/form
Content-Type: application/x-www-form-urlencoded

modelo=Honda CG 160&iotIdentificador=IOT001&setorId=1
```

#### Atualizar Moto
```http
PUT /moto/{id}
Content-Type: application/x-www-form-urlencoded

modelo=Honda CG 160 Fan&iotIdentificador=IOT001&setorId=1
```

#### Registrar Saída de Moto
```http
DELETE /moto/{id}
```

### 📊 Dashboard

#### Visualizar Dashboard
```http
GET /index
Accept: text/html
```

#### Dashboard Filtrado por Pátio
```http
GET /index?patioId=1
Accept: text/html
```

## 📊 Estrutura do Banco de Dados

### Tabelas Principais

1. **users**: Armazena usuários do sistema
2. **patio**: Pátios principais
3. **setor**: Setores dentro dos pátios
4. **moto**: Registro das motos com IoT

### Relacionamentos
- **Patio** → **Setor** (1:N)
- **Setor** → **Moto** (1:N)

## 🎨 Interface

O sistema utiliza **DaisyUI** com **TailwindCSS** para uma interface moderna e responsiva:

- Design escuro profissional
- Componentes reutilizáveis
- Navegação intuitiva
- Indicadores visuais de ocupação

## 🔧 Configuração de Desenvolvimento

### Perfis de Ambiente

#### Desenvolvimento (application.properties)
```properties
spring.profiles.active=dev
spring.datasource.url=jdbc:postgresql://localhost:5432/patio_vision
```

#### Produção (application-prod.properties)
```properties
spring.profiles.active=prod
# Configurações de produção
```

### Scripts de Migração

O projeto utiliza **Flyway** para versionamento do banco:

- `V1__create_table_user.sql`: Tabela de usuários
- `V2__create_table_patio.sql`: Tabela de pátios
- `V3__create_table_setor.sql`: Tabela de setores
- `V4__create_table_moto.sql`: Tabela de motos
- `V5__insert_patio_setor_moto.sql`: Dados de exemplo (dev)

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto é desenvolvido para fins acadêmicos na FIAP.

---

