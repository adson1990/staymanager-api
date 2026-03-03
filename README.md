🚀 StayManager API — Spring Boot + JWT Authentication

Backend desenvolvido com Java e Spring Boot, focado em segurança, arquitetura limpa e boas práticas modernas para APIs REST.

O projeto implementa autenticação stateless com JWT (Access + Refresh Token), validação robusta de dados e tratamento padronizado de exceções.

🛠 Stack Tecnológica

Java 17+
Spring Boot 3.x
Spring Security
JWT (JSON Web Token)
Spring Data JPA / Hibernate
PostgreSQL
Bean Validation (Jakarta Validation)
Maven
Git & GitHub

🔐 Segurança Implementada

Autenticação stateless com Access Token (JWT)
Refresh Token com rotação
Filtro customizado para validação do Bearer Token
Criptografia de senha com BCrypt
Tratamento adequado de:

401 Unauthorized (não autenticado)
403 Forbidden (sem permissão)

💡 Funcionalidades Atuais

Registro de usuários
Login com geração de Access + Refresh Token
Endpoint protegido por autenticação JWT
Validação de dados com @Valid
Tratamento global de exceções (@ControllerAdvice)
Separação clara entre:

Controller
Service
Repository
DTO
Camada de Segurança

🏗 Arquitetura e Boas Práticas

O projeto segue princípios de:

Clean Code
Separação de responsabilidades
Imutabilidade (uso de record para DTOs)
Desacoplamento entre entidade e camada de transporte
Arquitetura stateless (sem uso de sessão)

Fluxo da aplicação:

Request
   ↓
JWT Filter
   ↓
SecurityContext
   ↓
Controller
   ↓
Service
   ↓
Repository

▶️ Como Executar

1️⃣ Clone o projeto
git clone https://github.com/adson1990/staymanager-api.git
cd staymanager-api

2️⃣ Configure o banco de dados

Crie um banco PostgreSQL:

CREATE DATABASE staymanager;

Configure o application-dev.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/staymanager
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA

spring.jpa.hibernate.ddl-auto=update

3️⃣ Execute a aplicação

./mvnw spring-boot:run

A API estará disponível em:

http://localhost:8080

📌 Próximos Passos

Autorização baseada em Roles
Dockerização
Testes automatizados
Deploy em ambiente cloud