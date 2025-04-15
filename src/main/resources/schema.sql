DROP TABLE IF EXISTS `chapter`;
DROP TABLE IF EXISTS `book`;

CREATE TABLE `book`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `author`     varchar(255) NOT NULL,
    `isbn`       varchar(255) NOT NULL,
    `publisher`  varchar(255) NOT NULL,
    `summary`    varchar(255)          DEFAULT NULL,
    `title`      varchar(255) NOT NULL,
    `title_url`  varchar(255)          DEFAULT NULL,
    `created_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by` varchar(255)          DEFAULT NULL,
    `updated_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` varchar(255)          DEFAULT NULL,
    `deleted_at` timestamp             DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_book_isbn` (`isbn`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `chapter`
(
    `id`             bigint       NOT NULL AUTO_INCREMENT,
    `chapter_number` int          NOT NULL,
    `book_id`        bigint       NOT NULL,
    `name`           varchar(255) NOT NULL,
    `created_at`     timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by`     varchar(255)          DEFAULT NULL,
    `updated_at`     timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by`     varchar(255)          DEFAULT NULL,
    `deleted_at`     timestamp             DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_chapter_book_id` (`book_id`),
    CONSTRAINT `fk_chapter_book_id` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;