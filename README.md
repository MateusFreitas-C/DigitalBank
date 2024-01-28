# Digital Bank API
## Descrição:

Este projeto é uma API para um banco digital, desenvolvida utilizando Spring Boot e MariaDB. A API oferece funcionalidades de autenticação de usuários, permitindo que os mesmos realizem transações de crédito e débito em suas contas. Além disso, o sistema incorpora o conceito de faturas, proporcionando uma experiência completa de gerenciamento financeiro.

## Recursos Principais:

- Autenticação: Os usuários podem se autenticar na API para acessar suas contas.
- Transações Financeiras: Possibilidade de realizar transações de crédito e débito.
- Conceito de Faturas: Quando uma transação é feita no crédito é cadastrada uma fatura para o usuário.

## Documentação

# API do Digital Bank

Bem-vindo à documentação da API do Digital Bank. Aqui você encontrará informações sobre os endpoints disponíveis, suas funcionalidades e como interagir com a API.

## Documentação Swagger

A API é documentada usando o Swagger, uma ferramenta que facilita a visualização e teste de endpoints diretamente do navegador.

### Acessando a Documentação

Você pode acessar a documentação da API através do Swagger-UI. Após iniciar a aplicação, acesse o seguinte URL no seu navegador:

[Swagger-UI](http://localhost:8080/swagger-ui/)

### Endpoints Disponíveis

#### 1. Autenticação

- **Cadastro de Usuário:**
  - `POST /auth/signup`: Cria um novo usuário.
  
- **Login:**
  - `POST /auth/signin`: Realiza o login do usuário.

#### 2. Faturas

- **Consultar Faturas Não Pagas:**
  - `GET /invoices`: Obtém as faturas não pagas do usuário autenticado.

#### 3. Transações

- **Salvar Transação:**
  - `POST /transactions`: Realiza uma transação.
  
- **Consultar Transações:**
  - `GET /transactions?pageSize={pageSize}&pageNumber={pageNumber}`: Obtém as transações do usuário autenticado paginadas.

- **Pagar Fatura Atual:**
  - `GET /transactions/invoices/pay`: Paga a fatura atual do usuário autenticado.

### Exemplo de Uso

1. Faça o cadastro de um novo usuário:

```bash
curl -X POST -H "Content-Type: application/json" -d '{"name": "Nome", "cpf": "12345678900", "email": "email@example.com", "password": "senha123"}' http://localhost:8080/auth/signup
```
2. Faça login

```bash
curl -X POST -H "Content-Type: application/json" -d '{"cpf": "12345678900", "password": "senha123"}' http://localhost:8080/auth/signin
```

3. Consulte faturas não pagas

```bash
curl -H "Authorization: Bearer {TOKEN}" http://localhost:8080/invoices
```
4. Realize uma transação

```bash
curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer {TOKEN}" -d '{"destination": "ContaDestino", "amount": 100.00, "description": "Descrição da Transação", "type": "DEBIT", "installmentNumber": 1}' http://localhost:8080/transactions
```

5. Consulte transações

```bash
curl -H "Authorization: Bearer {TOKEN}" http://localhost:8080/transactions?pageSize=10&pageNumber=0
```
6. Pagar a fatura atual

```bash
curl -H "Authorization: Bearer {TOKEN}" http://localhost:8080/transactions/invoices/pay
```

