CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE categorias (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome        VARCHAR(100) NOT NULL UNIQUE,
    descricao   TEXT,
    ativo       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE fornecedores (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome        VARCHAR(100) NOT NULL UNIQUE,
    cnpj        VARCHAR(14) NOT NULL UNIQUE,
    telefone     VARCHAR(11) UNIQUE,
    email       VARCHAR(150) UNIQUE,
    ativo       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE produtos (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    codigo          VARCHAR(50)    NOT NULL UNIQUE,
    nome            VARCHAR(150)   NOT NULL,
    descricao       TEXT,
    preco_custo     NUMERIC(10, 2) NOT NULL,
    preco_venda     NUMERIC(10, 2) NOT NULL,
    quantidade      INTEGER        NOT NULL DEFAULT 0,
    estoque_minimo  INTEGER        NOT NULL DEFAULT 0,
    unidade         VARCHAR(20)    NOT NULL DEFAULT 'UN',
    categoria_id    UUID           REFERENCES categorias(id),
    fornecedor_id   UUID           REFERENCES fornecedores(id),
    ativo           BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP      NOT NULL DEFAULT NOW()
);

CREATE TABLE movimentacoes (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    produto_id          UUID           NOT NULL REFERENCES produtos(id),
    tipo                VARCHAR(20)    NOT NULL,
    quantidade          INTEGER        NOT NULL,
    quantidade_anterior INTEGER        NOT NULL,
    motivo              VARCHAR(255),
    created_at          TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP      NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_produtos_categoria    ON produtos(categoria_id);
CREATE INDEX idx_produtos_ativo        ON produtos(ativo);
CREATE INDEX idx_movimentacoes_produto ON movimentacoes(produto_id);
CREATE INDEX idx_produtos_fornecedor ON produtos(fornecedor_id);