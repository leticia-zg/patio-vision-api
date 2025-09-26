CREATE TABLE setor (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    capacidade_maxima INT NOT NULL,
    patio_id BIGINT,
    CONSTRAINT fk_setor_patio FOREIGN KEY (patio_id)
        REFERENCES patio(id)
        ON DELETE CASCADE
);
