🚀 StayManager API — Spring Boot + JWT Authentication

API REST para gestão de hotelaria desenvolvida com Java e Spring Boot, focada em segurança, arquitetura limpa e boas práticas modernas para APIs REST.

O projeto implementa autenticação stateless com JWT (Access + Refresh Token), controle de acesso baseado em roles, auditoria de login, paginação de resultados e documentação interativa com Swagger/OpenAPI.


🛠 Stack Tecnológica

Java 17+

Spring Boot 3.x

Spring Security

JWT (JSON Web Token)

Spring Data JPA / Hibernate

PostgreSQL

Bean Validation (Jakarta Validation)

Maven

Swagger / OpenAPI (SpringDoc)

Git & GitHub

Docker


🔐 Segurança Implementada

O sistema implementa um modelo de segurança stateless, sem uso de sessão.

Principais recursos:

*Autenticação com JWT Access Token*

*Refresh Token com rotação*

*Filtro customizado para validação do Bearer Token*

*Criptografia de senha com BCrypt*

*Controle de acesso baseado em roles*

Tratamento adequado de:

401 Unauthorized  → Usuário não autenticado
403 Forbidden     → Usuário sem permissão
Auditoria de Login

O sistema registra eventos de autenticação incluindo:

ID do usuário

Email utilizado

IP do cliente

User-Agent

Timestamp do login

Resultado da tentativa (SUCCESS ou FAILURE)

Motivo da falha (quando aplicável)

Essa auditoria permite rastreabilidade de acessos e demonstra boas práticas de segurança.


💡 Funcionalidades Atuais
🔑 Autenticação

Registro de usuários

Login com geração de Access Token + Refresh Token

Renovação de token via refresh

Auditoria de tentativas de login


👤 Gestão de Usuários

CRUD de usuários

Controle de acesso por roles


🏨 Gestão de Quartos

Cadastro de quartos

Atualização de status

Consulta de disponibilidade


📅 Sistema de Reservas

Criação de reservas

Check-in

Check-out

Cancelamento de reservas

Validação de conflito de datas


📊 Consultas com Paginação

Listagem de reservas

Listagem de quartos

Filtros por:

quarto

hóspede

status

suporte a:

?page=0
&size=10
&sort=checkInDate,desc

💻 Testing

Este projeto inclui testes automatizados *Spring Boot Tests*

📚 Documentação da API

A API possui documentação interativa via Swagger / OpenAPI.

Após iniciar a aplicação, acesse:

http://localhost:8080/swagger-ui.html

Ou:

http://localhost:8080/swagger-ui/index.html

🏗 Arquitetura e Boas Práticas

O projeto segue princípios de engenharia de software como:

Clean Code

Separação de responsabilidades

Arquitetura em camadas

DTOs para transporte de dados

Imutabilidade com record

Tratamento global de exceções (@RestControllerAdvice)

API stateless

Estrutura do Projeto
controller
service
repository
entity
dto
mapper
security
config
exception
Fluxo da aplicação
HTTP Request
      ↓
JWT Authentication Filter
      ↓
SecurityContext
      ↓
Controller
      ↓
Service
      ↓
Repository
      ↓
Database

🐳 Docker

Esta aplicação roda usando arquitetura de container com *multi-stage docker build*
- Spring Boot APi
- PostgreSQL Database

Docker compose orquestrando ambos serviços

Client
  |
  v
Spring Boot API (Docker)
  |
  v
PostgreSQL (Docker)

CI/CD

este projeto usa GitHub Actions para melhorar a qualidade do código

▶️ Como Executar o Projeto
1️⃣ Clone o repositório
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

ou

mvn spring-boot:run
4️⃣ Acesse a API
http://localhost:8080

Swagger UI:

http://localhost:8080/swagger-ui.html

📌 Próximos Passos

Melhorias planejadas para evolução do projeto:

Uso de GitHub Flow + Pull Request + pipeline

Deploy em ambiente cloud

Dashboard frontend consumindo a API

Monitoramento e métricas

👨‍💻 Autor

Adson Farias

GitHub:
https://github.com/adson1990

💡 Observação:
Este projeto foi desenvolvido como parte de estudo avançado em Java, Spring Boot, segurança de APIs e arquitetura backend moderna.
