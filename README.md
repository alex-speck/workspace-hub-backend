# WorkSpace Hub API

O WorkSpace Hub é uma aplicação backend construída com Spring Boot para gerenciar espaços de coworking e reservas. Ele oferece um CRUD completo para espaços, clientes e usuários, além de autenticação e documentação da API utilizando Swagger.

## 🚀 Tecnologias

- **Linguagem:** Java 17
- **Framework:** Spring Boot 4.0.4
- **Banco de Dados:** PostgreSQL
- **ORM:** Spring Data JPA
- **Documentação de API:** SpringDoc OpenAPI (Swagger)
- **Gerenciamento de Dependências:** Maven
- **Utilitários:** Lombok
- **Segurança:** JWT (JSON Web Token)

## 📋 Requisitos

- **Java JDK 17** ou superior
- **Maven** (ou use o wrapper `mvnw` fornecido)
- **PostgreSQL** instalado e em execução

## ⚙️ Variáveis de Ambiente

A aplicação utiliza as seguintes variáveis de ambiente (definidas no arquivo `application.properties` ou como variáveis de sistema):

- `DB_URL`: URL JDBC para o PostgreSQL (Padrão: `jdbc:postgresql://localhost:5432/workspacehub`)
- `DB_USER`: Usuário do banco de dados (Padrão: `postgres`)
- `DB_PASSWORD`: Senha do banco de dados (Padrão: `Sen@c2023`)
- `JWT_SECRET`: Chave secreta para assinatura dos tokens JWT

## 🛠️ Configuração e Instalação

1.  **Clonar o repositório:**
    ```bash
    git clone <repository-url>
    cd workspace-hub-backend
    ```

2.  **Configurar o Banco de Dados:**
    - Certifique-se de que o PostgreSQL está rodando.
    - Crie um banco de dados chamado `workspacehub`.
    - Atualize o arquivo `src/main/resources/application.properties` ou defina as variáveis de ambiente com suas credenciais.

3.  **Compilar o projeto:**
    ```bash
    ./mvnw clean install
    ```

## 🏃 Executando a Aplicação

Para iniciar a aplicação, execute:
```bash
./mvnw spring-boot:run
```
O servidor iniciará em `http://localhost:8080` (porta padrão do Spring Boot).

## 📖 Documentação da API (Swagger)

Com a aplicação em execução, você pode acessar a documentação interativa da API em:
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec:** `http://localhost:8080/v3/api-docs`

## 📁 Estrutura do Projeto

```text
src/main/java/com/projetofullstack/workspace_hub/
├── config/           # Classes de configuração (Cors, Swagger, JWT Filter)
├── controllers/      # Controllers REST (Endpoints)
├── model/
│   ├── dto/          # Objetos de Transferência de Dados (Request/Response)
│   ├── entities/     # Entidades JPA (Tabelas do banco de dados)
│   ├── enums/        # Enumerações (Status, Tipos)
│   └── repository/   # Repositórios JPA (Acesso a dados)
├── services/         # Serviços de negócio (TokenService)
└── WorkspaceHubApplication.java # Ponto de entrada principal
```

## 🧪 Testes

Para executar os testes unitários:
```bash
./mvnw test
```

## 📜 Licença

Este projeto está sob uma licença personalizada. Consulte o `pom.xml` para mais detalhes. (TODO: Definir uma licença específica como MIT ou Apache 2.0).

---
Desenvolvido por [Alex Speck](https://www.linkedin.com/in/alexspeck/).
