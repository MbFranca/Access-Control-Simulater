# Controle de Acesso (Java Console)

Projeto simples em Java (console) para autenticar usuário e, depois, autorizar acesso conforme o `Role`.

## Requisitos

- O usuário digita `username` e `password` via terminal.
- Os dados são validados contra o arquivo `users.txt` (no formato):
  - `username;password;role`
- `role` deve ser um destes valores (case-insensitive):
  - `ADMIN`
  - `SOLDADO`
  - `VISITANTE`

## Regras de acesso

- Login correto (autenticado) e `Role` = `ADMIN` ou `SOLDADO`:
  - `ACESSO LIBERADO`
- Login correto e `Role` = `VISITANTE`:
  - `ACESSO NEGADO - SEM PERMISSÃO`
- Login incorreto:
  - `ACESSO NEGADO`

## Estrutura do código

- `src/User.java`: modelo do usuário (username, password, role)
- `src/Role.java`: enum com `ADMIN`, `SOLDADO`, `VISITANTE`
- `src/UserRepository.java`: lê e interpreta o `users.txt`
- `src/AuthService.java`: autenticação (verifica username + password)
- `src/AuthorizationService.java`: autorização (verifica o `Role` após autenticar)
- `src/Main.java`: fluxo do programa (captura entrada, chama serviços e imprime mensagens)

## Como executar (Windows)

1. Garanta que o Java (JDK) esteja instalado e o `javac`/`java` funcionem no terminal.
2. Na pasta do projeto (`controlAcess`), compile:

   ```bat
   javac src\*.java
   ```

3. Rode:

   ```bat
   java -cp src Main
   ```

## Arquivo `users.txt`

Exemplo (o projeto já vem com um exemplo):

```txt
admin;123456;ADMIN
joao;senha123;SOLDADO
maria;abc123;VISITANTE
```

