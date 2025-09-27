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

## 🛠️ Tecnologias Utilizadas

- **Backend**: Spring Boot 3.5.4, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, TailwindCSS, DaisyUI
- **Banco de Dados**: PostgreSQL com Flyway Migrations
- **Autenticação**: OAuth2 (GitHub/Google) + Form-based
- **Containerização**: Docker e Docker Compose

## 🚀 Como Executar o Projeto

### Pré-requisitos
- Java 17+
- Docker e Docker Compose
- Gradle

### 1. Clone o repositório
```bash
git clone https://github.com/AnaCarolSant/patio-vision-api.git
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

