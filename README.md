# Sistema Escolar - Spring Boot

Projeto base com Spring Boot, Thymeleaf, JPA e MySQL.

## Requisitos
- Java 17+ (Java 21 também funciona)
- Maven
- MySQL com banco `sistema_escolar`

## Como executar
1. Ajuste o arquivo `src/main/resources/application.properties` com seu usuário e senha do MySQL.
2. Garanta que o banco e as tabelas já existam.
3. Execute:

```bash
mvn spring-boot:run
```

## Logos institucionais
Coloque os arquivos abaixo em `src/main/resources/static/img/`:
- `logo-prefeitura.png`
- `logo-educacao.png`

## Observação
Este projeto foi montado a partir da estrutura e dos arquivos desenvolvidos na conversa. Caso seu desktop tenha mudanças locais adicionais, pode ser necessário ajustar detalhes finos.
