# 🚀 User Management API - Spring Security & JWT

Este é um projeto didático focado em práticas modernas de desenvolvimento backend. O objetivo principal é implementar um sistema robusto de gerenciamento de hoteis, garantindo segurança e escalabilidade.

---

## 🛠 Tecnologias e Ferramentas

* **Linguagem:** Java 17+
* **Framework:** Spring Boot 3.x
* **Segurança:** Spring Security & JWT (JSON Web Token)
* **Persistência:** Spring Data JPA / Hibernate
* **Banco de Dados:** PostgreSQL (ou H2 para testes)
* **Ambiente de Dev:** Arch Linux + VS Code
* **Controle de Versão:** Git & GitHub

---

## 💡 Funcionalidades Implementadas

* **Registro de Usuários:** Endpoint para criação de novos usuários com persistência segura.
* **Autenticação Stateless:** Login via e-mail e senha retornando um token JWT assinado.
* **Validação de Dados:** Uso de Bean Validation (@Valid) para garantir a integridade dos inputs.
* **Tratamento de Exceções:** Handlers customizados para retornar erros de validação (FieldMessage) de forma amigável para o frontend.
* **Segurança de Senhas:** Criptografia utilizando BCrypt.
* **Revalidação de Tokens:** Uso de RefreshToken para gerar novo token de acesso.

---

## 🏗 Arquitetura e Boas Práticas

O projeto segue os princípios de **Clean Code** e separação de responsabilidades:

* **Padrão DTO (Data Transfer Object):** Utilizado para desacoplar a API das entidades do banco de dados, aumentando a segurança.
* **Controller -> Service -> Repository:** Fluxo lógico bem definido para facilitar a manutenção e testes.
* **Imutabilidade:** Preferência pelo uso de objetos finais e retornos explícitos de serviços (evitando efeitos colaterais).

---

## 🚀 Como Executar o Projeto

1. **Clone o repositório:**
   ```bash
   git clone [https://github.com/adson1990/staymanager-api.git](https://github.com/adson1990/staymanager-api.git)