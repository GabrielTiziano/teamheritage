-- V3__create_auth_tables.sql
-- Modelo de autenticacao: usuarios, scopes (permissoes) e o vinculo N:N entre eles.
-- IDs explicitos e deterministicos; sequences ajustadas no final (mesmo padrao da V2).
-- Senhas gravadas como hash BCrypt (cost 10), compativel com BCryptPasswordEncoder.

CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE scopes_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE users
(
    id       BIGINT PRIMARY KEY    DEFAULT nextval('users_seq'),
    name     VARCHAR(100) NOT NULL,
    email    VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    active   BOOLEAN      NOT NULL DEFAULT true
);

CREATE TABLE scopes
(
    id   BIGINT PRIMARY KEY DEFAULT nextval('scopes_seq'),
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users_scopes
(
    user_id  BIGINT NOT NULL,
    scope_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, scope_id),
    CONSTRAINT fk_users_scopes_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_users_scopes_scope FOREIGN KEY (scope_id) REFERENCES scopes (id)
);

-- Scopes: permissoes no formato recurso:acao
INSERT INTO scopes (id, name)
VALUES (1, 'admin:all'),
       (2, 'stadium:write'),
       (3, 'stadium:read'),
       (4, 'club:write'),
       (5, 'club:read'),
       (6, 'player:write'),
       (7, 'player:read');

-- Usuarios seed (senhas em hash BCrypt)
-- admin@teamheritage.com / admin123
-- user@teamheritage.com  / user123
INSERT INTO users (id, name, email, password, active)
VALUES (1, 'Admin', 'admin@teamheritage.com', '$2b$10$OZhKxTvsSZUczfFNWp6vpeQNxE.RmsiHYH4uftihCjxSNurdtX0bm', true),
       (2, 'User', 'user@teamheritage.com', '$2b$10$c7grmcfHFgYEMiYmpfIltOCPgCgTDY7E/iKhaYp3zSL.lpn6/Utqq', true);

-- Vinculos: admin recebe admin:all; user recebe apenas as leituras
INSERT INTO users_scopes (user_id, scope_id)
VALUES (1, 1),
       (2, 3),
       (2, 5),
       (2, 7);

-- Avanca as sequences para nao colidir com os IDs inseridos acima
SELECT setval('users_seq', 2, true);
SELECT setval('scopes_seq', 7, true);