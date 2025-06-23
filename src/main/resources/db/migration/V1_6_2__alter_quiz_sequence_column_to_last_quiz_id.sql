ALTER TABLE `user_quiz_set`
    CHANGE COLUMN `quiz_sequence` `last_quiz_id` BIGINT DEFAULT NULL;