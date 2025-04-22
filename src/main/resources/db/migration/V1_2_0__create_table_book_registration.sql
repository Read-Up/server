DROP TABLE IF EXISTS `book_registration`;

CREATE TABLE `book_registration`
(
    `id`                  bigint       NOT NULL AUTO_INCREMENT,
    `requester_id`        bigint       NOT NULL,
    `processor_id`        bigint,
    `title`               varchar(255) NOT NULL,
    `isbn`                varchar(20)  NOT NULL UNIQUE,
    `registration_status` varchar(50)  NOT NULL,
    `created_at`          timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by`          bigint                DEFAULT NULL,
    `updated_at`          timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by`          bigint                DEFAULT NULL,
    `deleted_at`          timestamp             DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_book_isbn` (`isbn`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;