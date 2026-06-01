# WorkSpace Hub

O **WorkSpace Hub** é uma solução completa para gerenciamento de espaços de coworking e reservas. O projeto consiste em uma API backend robusta e um cliente desktop para administração.

---

## 🏗️ Estrutura do Projeto

O repositório está organizado como um monorepo contendo:

1.  **Backend (`/`)**: API REST construída com Spring Boot.
2.  **Desktop (`/desktop`)**: Aplicação cliente construída com JavaFX.

---

## 🚀 Backend (API)

A API gerencia o core do negócio, incluindo espaços, clientes, usuários, reservas e autenticação.

### 🛠️ Tecnologias
- **Linguagem:** Java 17
- **Framework:** Spring Boot 4.0.4
- **Banco de Dados:** PostgreSQL
- **ORM:** Spring Data JPA
- **Segurança:** Spring Security + JWT
- **Documentação:** SpringDoc OpenAPI (Swagger)

### ⚙️ Configuração
As configurações estão no arquivo `src/main/resources/application.properties`. As principais chaves são:
- `spring.datasource.url`: URL do banco de dados.
- `spring.datasource.username` / `password`: Credenciais do banco.
- `spring.security.jwt.secret`: Chave secreta para tokens JWT.

### 🏃 Execução
```bash
./mvnw clean install
./mvnw spring-boot:run
```
A API estará disponível em `http://localhost:8080`.

### 📖 Documentação (Swagger)
Acesse a documentação interativa em:
- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI Spec:** `http://localhost:8080/v3/api-docs`

---

## 💻 Desktop (Cliente)

Aplicação administrativa para gerenciar o sistema de forma visual.

### 🛠️ Tecnologias
- **Linguagem:** Java 23
- **Framework:** JavaFX 21
- **Interface:** FXML

### 🏃 Execução
Navegue até a pasta `desktop` e execute:
```bash
cd desktop
./mvnw clean javafx:run
```

---

## 📁 Organização de Pastas (Backend)

O backend segue uma arquitetura em camadas:
- `application`: DTOs e serviços de aplicação (Regras de negócio).
- `domain`: Entidades, Enums, Repositórios e Objetos de Valor (Domínio).
- `infrastructure`: Configurações, Segurança e Tratamento de Exceções.
- `presentation`: Controllers REST (Endpoints).

---

## 📜 Licença

Este projeto está sob licença personalizada. Consulte o `pom.xml` para detalhes.

---
Desenvolvido por [Alex Speck](https://www.linkedin.com/in/alexspeck/).
