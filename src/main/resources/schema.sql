CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(16) PRIMARY KEY,
    password_hash VARCHAR(255),
    name VARCHAR(255)
);