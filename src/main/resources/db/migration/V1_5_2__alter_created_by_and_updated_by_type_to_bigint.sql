ALTER TABLE `user_terms_consent`
    MODIFY COLUMN `created_by` BIGINT DEFAULT NULL,
    MODIFY COLUMN `updated_by` BIGINT DEFAULT NULL;