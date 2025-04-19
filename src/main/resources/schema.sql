DROP TABLE IF EXISTS `chapter`;
DROP TABLE IF EXISTS `book`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `social_account`;

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

CREATE TABLE user
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `nickname`   varchar(255) NOT NULL,
    `created_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by` varchar(255)          DEFAULT NULL,
    `updated_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` varchar(255)          DEFAULT NULL,
    `deleted_at` timestamp             DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `social_account`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT,
    `user_id`      bigint                DEFAULT NULL,
    `email`        varchar(255) NOT NULL,
    `provider`     varchar(255) NOT NULL,
    `provider_uid` varchar(255) NOT NULL,
    `created_at`   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by`   varchar(255)          DEFAULT NULL,
    `updated_at`   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by`   varchar(255)          DEFAULT NULL,
    `deleted_at`   timestamp             DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_social_account_user_id` (`user_id`),
    CONSTRAINT `fk_social_account_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;