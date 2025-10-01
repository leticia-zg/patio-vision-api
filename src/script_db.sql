-- ============================================================================
-- Script: script_bd.sql
-- Descrição: DDL consolidado do esquema após as migrações Flyway (V1..V6)
--            Compatível com PostgreSQL (Azure Database for PostgreSQL Flexible Server)
-- ============================================================================

-- ============================================================================
-- Configurações gerais
-- ============================================================================
-- Garante o schema padrão
CREATE SCHEMA IF NOT EXISTS public;

-- Opcional: garante search_path
-- ALTER ROLE CURRENT_USER IN DATABASE CURRENT_DATABASE SET search_path TO public;

-- ============================================================================
-- Tabela: users
-- Migrações relacionadas: V1__create_table_user.sql, V6__add_password_field_to_user.sql
-- ============================================================================
DROP TABLE IF EXISTS public.users CASCADE;
CREATE TABLE public.users (
    id              BIGSERIAL       PRIMARY KEY,              -- chave primária
    name            VARCHAR(120)    NOT NULL,                 -- nome do usuário
    email           VARCHAR(180)    NOT NULL UNIQUE,          -- e-mail único para login
    password        VARCHAR(255)    NOT NULL,                 -- senha (armazenada com hash/encoder)
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),   -- data de criação
    updated_at      TIMESTAMP       NOT NULL DEFAULT NOW()    -- data de atualização (opcional: trigger para atualizar em UPDATE)
);
COMMENT ON TABLE  public.users IS 'Usuários do sistema';
COMMENT ON COLUMN public.users.password IS 'Senha (armazenada via hash/encoder) — adicionada na migração V6.';

-- ============================================================================
-- Tabela: patio
-- Migração: V2__create_table_patio.sql
-- ============================================================================
DROP TABLE IF EXISTS public.patio CASCADE;
CREATE TABLE public.patio (
    id          BIGSERIAL       PRIMARY KEY,          -- chave primária
    nome        VARCHAR(120)    NOT NULL,             -- nome do pátio
    endereco    VARCHAR(255)    NULL,                 -- endereço (opcional)
    cidade      VARCHAR(120)    NULL,                 -- cidade (opcional)
    estado      VARCHAR(2)      NULL,                 -- UF (opcional)
    criado_em   TIMESTAMP       NOT NULL DEFAULT NOW()
);
COMMENT ON TABLE public.patio IS 'Pátios cadastrados';

-- ============================================================================
-- Tabela: setor
-- Migração: V3__create_table_setor.sql
-- ============================================================================
DROP TABLE IF EXISTS public.setor CASCADE;
CREATE TABLE public.setor (
    id          BIGSERIAL       PRIMARY KEY,          -- chave primária
    nome        VARCHAR(120)    NOT NULL,             -- nome do setor
    capacidade  INTEGER         NULL,                 -- capacidade (opcional)
    patio_id    BIGINT          NOT NULL,             -- FK -> patio.id
    criado_em   TIMESTAMP       NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_setor_patio FOREIGN KEY (patio_id)
        REFERENCES public.patio(id)
        ON DELETE CASCADE
);
COMMENT ON TABLE public.setor IS 'Setores pertencentes a um pátio';

-- Índice auxiliar para buscas por pátio
CREATE INDEX IF NOT EXISTS idx_setor_patio ON public.setor(patio_id);

-- ============================================================================
-- Tabela: moto
-- Migração: V4__create_table_moto.sql
-- OBS: FK com ON DELETE SET NULL exige coluna NULLABLE
-- ============================================================================
DROP TABLE IF EXISTS public.moto CASCADE;
CREATE TABLE public.moto (
    id          BIGSERIAL       PRIMARY KEY,          -- chave primária
    placa       VARCHAR(20)     NOT NULL UNIQUE,      -- placa única
    modelo      VARCHAR(120)    NOT NULL,             -- modelo
    cor         VARCHAR(60)     NULL,                 -- cor (opcional)
    setor_id    BIGINT          NULL,                 -- FK -> setor.id (posição atual)
    criado_em   TIMESTAMP       NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_moto_setor FOREIGN KEY (setor_id)
        REFERENCES public.setor(id)
        ON DELETE SET NULL
);
COMMENT ON TABLE public.moto IS 'Motos cadastradas e alocadas em setores';

-- Índice auxiliar para buscas por setor
CREATE INDEX IF NOT EXISTS idx_moto_setor ON public.moto(setor_id);

-- ============================================================================
-- Dados iniciais (V5__insert_patio_setor_moto.sql)
-- DICA: Os inserts abaixo são exemplos; adapte conforme sua migração V5.
-- ============================================================================
-- INSERT INTO public.patio (nome, endereco, cidade, estado) VALUES
--   ('Pátio Central', 'Av. Exemplo, 1000', 'São Paulo', 'SP');

-- INSERT INTO public.setor (nome, capacidade, patio_id) VALUES
--   ('A', 50, 1),
--   ('B', 35, 1);

-- INSERT INTO public.moto (placa, modelo, cor, setor_id) VALUES
--   ('ABC1D23', 'CG 160', 'Preta', 1),
--   ('DEF4G56', 'Biz 125', 'Vermelha', 2);

-- INSERT INTO public.users (name, email, password) VALUES
--   ('Admin', 'admin@exemplo.com', '$2a$10$HASH_AQUI');

-- ============================================================================
-- Fim do script
-- ============================================================================
