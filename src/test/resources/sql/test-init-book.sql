DROP TABLE IF EXISTS chapter;
DROP TABLE IF EXISTS book;

CREATE TABLE book
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    author     VARCHAR(255) NOT NULL,
    isbn       VARCHAR(255) NOT NULL,
    publisher  VARCHAR(255) NOT NULL,
    summary    VARCHAR(255),
    title      VARCHAR(255) NOT NULL,
    title_url  VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT,
    deleted_at TIMESTAMP,
    UNIQUE (isbn)
);
