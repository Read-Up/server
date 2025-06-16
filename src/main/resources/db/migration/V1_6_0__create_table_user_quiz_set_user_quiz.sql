DROP TABLE IF EXISTS `user_quiz`;
DROP TABLE IF EXISTS `user_quiz_set`;

CREATE TABLE `user_quiz_set`
(
    `id`                        bigint    NOT NULL AUTO_INCREMENT,
    `quiz_set_id`               bigint    NOT NULL,
    `is_evaluated`              boolean   NOT NULL DEFAULT FALSE,
    `quiz_sequence`             int       NOT NULL DEFAULT 1,
    `correct_answer_average`    double             DEFAULT NULL,
    `like_score`                int                DEFAULT NULL,
    `created_at`                timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by`                bigint             DEFAULT NULL,
    `updated_at`                timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by`                bigint             DEFAULT NULL,
    `deleted_at`                timestamp          DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_quiz_set_id_created_by` (`quiz_set_id`, `created_by`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `user_quiz`
(
    `id`                bigint       NOT NULL AUTO_INCREMENT,
    `quiz_id`           bigint       NOT NULL,
    `attempt_count`     int          NOT NULL   DEFAULT 0,
    `is_correct`        boolean                 DEFAULT NULL,
    `user_quiz_set_id`  bigint       NOT NULL,
    `created_at`        timestamp    NOT NULL   DEFAULT CURRENT_TIMESTAMP,
    `created_by`        bigint                  DEFAULT NULL,
    `updated_at`        timestamp    NOT NULL   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by`        bigint                  DEFAULT NULL,
    `deleted_at`        timestamp               DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_quiz_id_created_by` (`quiz_id`, `created_by`),
    KEY `fk_user_quiz_user_quiz_set_id` (`user_quiz_set_id`),
    FOREIGN KEY `fk_user_quiz_user_quiz_set_id` (`user_quiz_set_id`) REFERENCES `user_quiz_set` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
