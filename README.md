# Controle de Acesso (Java Console + SQLite)

Sistema de controle de acesso em Java (console), com persistência em SQLite via JDBC, CRUD completo de usuários e registro de logs de tentativa de acesso.

## Funcionalidades

- Login com autenticação e autorização por `Role`
- CRUD de usuários:
  - Cadastrar
  - Listar
  - Atualizar
  - Deletar
- Registro de logs de acesso em banco (`LIBERADO`/`NEGADO`)

## Regras de negócio

- `username` não pode ser vazio
- `password` deve ter no mínimo 4 caracteres
- `role` deve ser:
  - `ADMIN`
  - `SOLDADO`
  - `VISITANTE`

## Regras de acesso

- Login correto e `Role` = `ADMIN` ou `SOLDADO`:
  - `ACESSO LIBERADO`
- Login correto e `Role` = `VISITANTE`:
  - `ACESSO NEGADO - SEM PERMISSÃO`
- Login incorreto:
  - `ACESSO NEGADO`

## Menu (console)

- `1` - Login
- `2` - Cadastrar usuário
- `3` - Listar usuários
- `4` - Atualizar usuário
- `5` - Deletar usuário
- `0` - Sair

## Banco de dados (SQLite)

Arquivo: `access_control.db`

Tabelas criadas automaticamente:

- `users`
  - `id` (PK, autoincrement)
  - `username` (único)
  - `password`
  - `role`
- `access_logs`
  - `id` (PK, autoincrement)
  - `username`
  - `access_time` (data/hora)
  - `status` (`LIBERADO` ou `NEGADO`)

## Estrutura do código

- `src/DatabaseConnection.java`: conexão JDBC com SQLite
- `src/User.java`: modelo de usuário
- `src/Role.java`: enum de papéis
- `src/UserRepository.java`: acesso a dados de usuários (CRUD)
- `src/UserService.java`: regras de negócio e validações
- `src/AuthService.java`: autenticação por `username` + `password`
- `src/AuthorizationService.java`: autorização por `Role`
- `src/AccessLogRepository.java`: gravação de logs de acesso
- `src/Main.java`: interface/menu em console

## Como executar (Windows / PowerShell)

1. Garanta que o Java (JDK) esteja instalado.
2. Compile com o driver SQLite no classpath:

```powershell
javac -cp ".;sqlite-jdbc-3.46.1.3.jar" src\*.java
```

3. Execute:

```powershell
$env:CLASSPATH="src;sqlite-jdbc-3.46.1.3.jar"
java Main
```

