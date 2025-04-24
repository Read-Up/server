DROP TABLE IF EXISTS chapter;

CREATE TABLE chapter
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    chapter_number INT          NOT NULL,
    book_id        BIGINT       NOT NULL,
    name           VARCHAR(255) NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by     BIGINT,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by     BIGINT,
    deleted_at     TIMESTAMP,

    FOREIGN KEY (book_id) REFERENCES book (id)
);