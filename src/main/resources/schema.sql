CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login_id VARCHAR(255) UNIQUE,
    password_hash VARCHAR(255),
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS chatroom {
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
};

CREATE TABLE IF NOT EXISTS chatroom_participants {
    chatroom_id BIGINT REFERENCES chatroom(chatroom_id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (chatroom_id, user_id)
};
