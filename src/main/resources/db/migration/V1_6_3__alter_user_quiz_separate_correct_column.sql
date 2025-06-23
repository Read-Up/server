ALTER TABLE `user_quiz`
    CHANGE COLUMN `is_correct` `current_attempt_correct` boolean DEFAULT NULL;

ALTER TABLE `user_quiz`
    ADD COLUMN `first_attempt_correct` boolean DEFAULT NULL;