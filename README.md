# TeamHeritage

API REST de futebol para gerenciar estádios, clubes e jogadores. O acesso é protegido por autenticação stateless com JWT assinado por RSA, e cada rota exige uma permissão específica (scope). A base já sobe populada com dados reais dos principais clubes das grandes ligas europeias e do Brasileirão.

![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![OAuth2](https://img.shields.io/badge/OAuth2-2F80ED?style=for-the-badge&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![MapStruct](https://img.shields.io/badge/MapStruct-FF6C37?style=for-the-badge&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-A4133C?style=for-the-badge)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-4DB33D?style=for-the-badge)
![Testcontainers](https://img.shields.io/badge/Testcontainers-291A38?style=for-the-badge)
![JaCoCo](https://img.shields.io/badge/JaCoCo-EC1C24?style=for-the-badge)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)

## Sumário

- [Sobre](#sobre)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Segurança](#segurança)
- [Banco de dados e migrations](#banco-de-dados-e-migrations)
- [Como rodar](#como-rodar)
- [Autenticando na API](#autenticando-na-api)
- [Endpoints](#endpoints)
- [Testes](#testes)
- [CI](#ci)
- [Estrutura de pastas](#estrutura-de-pastas)
- [Contato](#contato)

## Sobre

Montei o TeamHeritage para exercitar, de ponta a ponta, um fluxo de back-end parecido com o de produção: schema versionado com migrations, DTOs separados das entidades, regra de negócio isolada nos services, segurança por token assinado, erros padronizados e uma suíte de testes com cobertura medida.

A ideia não é só ter o CRUD funcionando. É ter cada decisão de arquitetura justificável: por que os controllers não conhecem regra de negócio, por que a entidade nunca sai na resposta, por que a autorização é por scope e não por role fixa.

## Tecnologias

| Área | O que usei |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 4.1.0 |
| Segurança | Spring Security como OAuth2 Resource Server, JWT com assinatura RSA (RS256) |
| Persistência | Spring Data JPA sobre Hibernate |
| Banco | PostgreSQL 16 |
| Migrations | Flyway |
| Mapeamento DTO / entidade | MapStruct |
| Boilerplate | Lombok |
| Validação | Bean Validation (Jakarta) |
| Container | Docker e Docker Compose |
| Testes de unidade | JUnit 5 com Mockito |
| Testes de integração | Testcontainers (Postgres real subindo em container) |
| Cobertura | JaCoCo |
| CI | GitHub Actions |

## Arquitetura

A requisição atravessa as camadas nesta ordem:

```
Controller  ->  Service  ->  Repository  ->  PostgreSQL
     |             |
   DTOs        Mapper (MapStruct)
```

O controller recebe a requisição, valida a entrada com Bean Validation e chama o service. Ele não tem regra de negócio nenhuma.

O service é onde a lógica vive. É também a camada que mais testei, porque é a que quebra o sistema quando dá errado.

O repository é a interface do Spring Data JPA que fala com o banco.

Os DTOs separam o contrato da API do modelo de persistência. A entidade JPA nunca vai direto na resposta, o que evita vazar detalhe interno e acoplar o cliente ao banco.

O MapStruct faz a conversão entre DTO e entidade gerando o código em tempo de compilação. Sem reflexão em runtime, o que é mais rápido e o código gerado dá pra ler.

Os erros passam por um `@RestControllerAdvice` global que devolve `ProblemDetail` (RFC 9457) com o status HTTP certo para cada caso: 404 quando o recurso não existe, 409 em conflito, 400 em payload inválido, 401 e 403 nos casos de autenticação e autorização.

## Segurança

A API é um OAuth2 Resource Server. Ela mesma emite o token no login e valida esse token nas requisições seguintes, usando um par de chaves RSA.

No `POST /login` a senha é conferida com BCrypt. Se bater, um JWT é gerado e assinado com a chave privada. Nas rotas protegidas o token é validado com a chave pública, e as permissões saem da claim `scope`. A autorização acontece por método, com `@PreAuthorize`.

Os scopes seguem o formato `recurso:acao`:

| Scope | Dá acesso a |
|---|---|
| `admin:all` | tudo |
| `stadium:read` / `stadium:write` | ler / escrever estádios |
| `club:read` / `club:write` | ler / escrever clubes |
| `player:read` / `player:write` | ler / escrever jogadores |

Dois usuários já vêm cadastrados pelo seed:

| E-mail | Senha | Permissões |
|---|---|---|
| `admin@teamheritage.com` | `admin123` | `admin:all` |
| `user@teamheritage.com` | `user123` | só as leituras |

As chaves RSA ficam fora do controle de versão. A seção [Como rodar](#como-rodar) mostra como gerar o par localmente.

## Banco de dados e migrations

O schema é versionado com Flyway. Cada mudança vira um arquivo `V<n>__descricao.sql` que roda sozinho quando a aplicação sobe. O `ddl-auto` está em `validate`, então o Hibernate só confere se o mapeamento das entidades bate com o schema e nunca altera o banco por conta própria.

| Migration | O que faz |
|---|---|
| `V1__create_initial_tables.sql` | cria as tabelas de estádio, clube e jogador |
| `V2__insert_seed_data.sql` | popula 60 estádios, 60 clubes e 600 jogadores reais |
| `V3__create_auth_tables.sql` | cria `users`, `scopes` e o vínculo N:N, já com usuários e permissões |

## Como rodar

Você vai precisar de Java 17 (JDK), Docker com o Docker Desktop aberto e Maven (ou o wrapper `./mvnw`).

Um aviso que custou algumas horas de debug: o projeto compila para Java 17, mas se a JVM que roda o build for muito nova (Java 26, por exemplo), o agente do JaCoCo não consegue instrumentar o bytecode e enche o console de erro. Cheque com `mvn -version` que o Maven está usando o Java 17.

Suba o banco:

```bash
docker compose up -d
```

Isso levanta um PostgreSQL 16 com banco, usuário e senha `teamheritage` na porta `5432`.

Gere o par de chaves RSA dentro de `src/main/resources` (esses arquivos são ignorados pelo Git):

```bash
openssl genrsa -out keypair.pem 2048
openssl rsa -in keypair.pem -pubout -out src/main/resources/app.pub
openssl pkcs8 -topk8 -inform PEM -in keypair.pem -out src/main/resources/app.key -nocrypt
rm keypair.pem
```

Rode a aplicação:

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`. O Flyway aplica as migrations e popula a base na primeira execução.

## Autenticando na API

Faça login para pegar o token:

```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@teamheritage.com","password":"admin123"}'
```

A resposta traz o token e o tempo de expiração em segundos:

```json
{ "accessToken": "eyJhbGciOiJSUzI1Ni...", "expiresIn": 900 }
```

Use o token no header das rotas protegidas:

```bash
curl http://localhost:8080/clubs \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1Ni..."
```

O token vale 15 minutos.

## Endpoints

| Método | Rota | Scope | Descrição |
|---|---|---|---|
| `POST` | `/login` | público | autentica e devolve o JWT |
| `POST` | `/users` | público | cadastra um usuário |
| `GET` | `/stadiums` | `stadium:read` | lista estádios |
| `POST` / `PUT` | `/stadiums` | `stadium:write` | cria e atualiza estádio |
| `GET` | `/clubs` | `club:read` | lista clubes |
| `POST` / `PUT` | `/clubs` | `club:write` | cria e atualiza clube |
| `GET` | `/clubs/{id}/players` | `player:read` | lista os jogadores de um clube |
| `GET` | `/players` | `player:read` | lista jogadores |
| `POST` / `PUT` | `/players` | `player:write` | cria e atualiza jogador |

## Testes

São 57 testes automatizados em duas frentes.

Os de unidade usam JUnit 5 com Mockito e testam os services isolados, com repositório e mapper mockados. Cobrem tanto o caminho feliz quanto os de exceção, então a camada de negócio fica em 92% de cobertura.

Os de integração usam Testcontainers para subir um PostgreSQL de verdade num container e exercitam os controllers do HTTP até o banco, incluindo os cenários de autorização (o 200 de quem tem o scope e o 403 de quem não tem). Foi um teste desses que pegou um bug real onde um acesso negado voltava como 500 em vez de 403.

Para rodar tudo e gerar o relatório de cobertura:

```bash
mvn verify
```

O relatório do JaCoCo fica em `target/site/jacoco/index.html`. Hoje o projeto está em 79% de cobertura de instruções no geral.

## CI

A cada push ou pull request na `main`, o GitHub Actions roda `mvn verify` num runner limpo. Ele sobe o Postgres via Testcontainers e gera um par de chaves RSA efêmero só para o contexto da aplicação carregar. Nada entra na branch principal sem passar pela suíte inteira.

## Estrutura de pastas

```
src
├── main
│   ├── java/com/gabriel/tiziano/teamheritage
│   │   ├── config          SecurityConfig e CORS
│   │   ├── controller      endpoints REST
│   │   ├── dto             request e response
│   │   ├── entities        entidades JPA e enums
│   │   ├── exception       exceções e handler global
│   │   ├── mapper          interfaces MapStruct
│   │   ├── repository      Spring Data JPA
│   │   └── service         regra de negócio
│   └── resources
│       ├── db/migration    scripts Flyway
│       └── application.yaml
└── test
    └── java/com/gabriel/tiziano/teamheritage
        ├── integration     testes de controller com Testcontainers
        ├── mapper          testes de mapper
        └── service         testes de service com Mockito
```

## Contato

Gabriel Tiziano

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/gabrieltiziano/)
[![Gmail](https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:gghiaronitiziano@gmail.com)
