CREATE TABLE configuracoes (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    chave       VARCHAR(100) NOT NULL UNIQUE,
    valor       VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

INSERT INTO configuracoes (id, chave, valor, created_at, updated_at)
VALUES (uuid_generate_v4(), 'margem_padrao', '30', NOW(), NOW());