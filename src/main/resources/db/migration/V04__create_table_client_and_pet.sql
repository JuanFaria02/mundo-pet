CREATE TABLE client (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    document_number VARCHAR(255) NOT NULL
);

CREATE TABLE pet (
     id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     type VARCHAR(255) NOT NULL,
     microchip VARCHAR(255) NOT NULL UNIQUE,
     active BOOLEAN DEFAULT TRUE,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     client_id BIGINT,
     CONSTRAINT client_fk_pet FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE SET NULL
);

