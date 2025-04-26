DROP TABLE IF EXISTS `quiz_set`;
DROP TABLE IF EXISTS `quiz`;
DROP TABLE IF EXISTS `quiz_option`;

CREATE TABLE `quiz_set`
(
    `id`         bigint    NOT NULL AUTO_INCREMENT,
    `chapter_id` bigint    NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by` bigint             DEFAULT NULL,
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` bigint             DEFAULT NULL,
    `deleted_at` timestamp          DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `quiz`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `quiz_set_id` bigint       NOT NULL,
    `question`    varchar(150) NOT NULL,
    `explanation` varchar(255) NOT NULL,
    `created_at`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by`  bigint                DEFAULT NULL,
    `updated_at`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by`  bigint                DEFAULT NULL,
    `deleted_at`  timestamp             DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_quiz_quiz_set_id` (`quiz_set_id`),
    FOREIGN KEY `fk_quiz_quiz_set_id` (`quiz_set_id`) REFERENCES `quiz_set` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `quiz_option`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `quiz_id`    bigint       NOT NULL,
    `content`    varchar(255) NOT NULL,
    `is_correct` boolean      NOT NULL,
    `created_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by` bigint                DEFAULT NULL,
    `updated_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` bigint                DEFAULT NULL,
    `deleted_at` timestamp             DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_quiz_option_quiz_id` (`quiz_id`),
    FOREIGN KEY `fk_quiz_option_quiz_id` (`quiz_id`) REFERENCES `quiz` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;