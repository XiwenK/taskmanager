# CREATE DATABASE IF NOT EXISTS taskmanager;

# USE taskmanager;

CREATE TABLE IF NOT EXISTS tasks
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    completed   BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  DATETIME(6)  NOT NULL,
    updated_at  DATETIME(6),
    CONSTRAINT chk_completed CHECK (completed IN (0, 1))
);

