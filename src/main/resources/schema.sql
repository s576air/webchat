CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login_id VARCHAR(255) UNIQUE,
    password_hash VARCHAR(255),
    name VARCHAR(255),
    friend_code_tag CHAR(8)
);

CREATE TABLE IF NOT EXISTS friends (
    id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    id2 BIGINT REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (id, id2),
    CHECK (id < id2)
);

CREATE TABLE IF NOT EXISTS chatroom (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS chatroom_participants (
    chatroom_id BIGINT REFERENCES chatroom(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (chatroom_id, user_id)
);

CREATE TABLE IF NOT EXISTS chat (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    chatroom_id BIGINT REFERENCES chatroom(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    is_text BOOLEAN,
    content_id BIGINT,
    sent_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX chat_chatroom_id_and_time ON chat (chatroom_id, sent_time);

CREATE TABLE IF NOT EXISTS text_chat (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    text TEXT
);

CREATE TABLE IF NOT EXISTS data_chat (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ext VARCHAR(32),
    data BLOB
);