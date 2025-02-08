CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('ADMIN', 'USER'))
);

CREATE TABLE partners (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE addresses (
    id          SERIAL PRIMARY KEY,
    partner_id  INT          NOT NULL,
    street      VARCHAR(255) NOT NULL,
    city        VARCHAR(100) NOT NULL,
    FOREIGN KEY (partner_id) REFERENCES partners (id) ON DELETE CASCADE
);