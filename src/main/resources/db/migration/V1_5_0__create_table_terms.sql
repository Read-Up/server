DROP TABLE IF EXISTS `user_terms_consent`;
DROP TABLE IF EXISTS `terms_version`;
DROP TABLE IF EXISTS `terms`;

CREATE TABLE `terms`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `code`       varchar(50)  NOT NULL,
    `title`      varchar(100) NOT NULL,
    `created_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by` bigint                DEFAULT NULL,
    `updated_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` bigint                DEFAULT NULL,
    `deleted_at` timestamp             DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `terms_version`
(
    `id`             bigint      NOT NULL AUTO_INCREMENT,
    `terms_id`       bigint      NOT NULL,
    `version`        varchar(20) NOT NULL,
    `content`        TEXT        NOT NULL,
    `effective_date` DATETIME    NOT NULL,
    `is_required`    boolean     NOT NULL,
    `created_at`     timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by`     bigint               DEFAULT NULL,
    `updated_at`     timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by`     bigint               DEFAULT NULL,
    `deleted_at`     timestamp            DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_terms_id_version` (`terms_id`, `version`),
    KEY `idx_terms_version_terms_id` (`terms_id`),
    CONSTRAINT `fk_terms_version_terms_id` FOREIGN KEY (`terms_id`) REFERENCES `terms` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `user_terms_consent`
(
    `id`               bigint    NOT NULL AUTO_INCREMENT,
    `user_id`          bigint    NOT NULL,
    `terms_id`         bigint    NOT NULL,
    `terms_version_id` bigint    NOT NULL,
    `is_consent`       boolean   NOT NULL,
    `created_at`       timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by`       varchar(255)       DEFAULT NULL,
    `updated_at`       timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by`       varchar(255)       DEFAULT NULL,
    `deleted_at`       timestamp          DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_user_terms_consent_user_id` (`user_id`),
    KEY `idx_user_terms_consent_terms_id` (`terms_id`),
    KEY `idx_user_terms_consent_terms_version_id` (`terms_version_id`),
    CONSTRAINT `fk_user_terms_consent_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_user_terms_consent_terms_id` FOREIGN KEY (`terms_id`) REFERENCES `terms` (`id`),
    CONSTRAINT `fk_user_terms_consent_terms_version_id` FOREIGN KEY (`terms_version_id`) REFERENCES `terms_version` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;