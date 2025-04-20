DROP TABLE IF EXISTS `chapter`;
DROP TABLE IF EXISTS `book`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `social_account`;
DROP TABLE IF EXISTS `terms`;
DROP TABLE IF EXISTS `terms_version`;
DROP TABLE IF EXISTS `user_terms_consent`;

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

CREATE TABLE `terms`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `code`  varchar(50)  NOT NULL,
    `title`      varchar(100) NOT NULL,
    `created_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by` varchar(255)          DEFAULT NULL,
    `updated_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` varchar(255)          DEFAULT NULL,
    `deleted_at` timestamp             DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `terms_version`
(
    `id`            bigint       NOT NULL AUTO_INCREMENT,
    `terms_id`      bigint       NOT NULL,
    `version`       varchar(20)  NOT NULL,
    `content`       TEXT         NOT NULL,
    `effective_date` DATETIME     NOT NULL,
    `is_required`   boolean      NOT NULL,
    `created_at`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_by`    varchar(255)          DEFAULT NULL,
    `updated_at`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by`    varchar(255)          DEFAULT NULL,
    `deleted_at`    timestamp             DEFAULT NULL,
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