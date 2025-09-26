CREATE TABLE moto (
    id BIGSERIAL PRIMARY KEY,
    modelo VARCHAR(255) NOT NULL,
    iot_identificador VARCHAR(255) NOT NULL UNIQUE,
    data_entrada TIMESTAMP NOT NULL,
    data_saida TIMESTAMP,
    setor_id BIGINT,
    CONSTRAINT fk_moto_setor FOREIGN KEY (setor_id)
        REFERENCES setor(id)
        ON DELETE CASCADE
);
