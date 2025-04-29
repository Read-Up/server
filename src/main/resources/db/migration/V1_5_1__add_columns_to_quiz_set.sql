ALTER TABLE `quiz_set`
    ADD COLUMN `book_id` BIGINT NOT NULL,
    ADD COLUMN `participant_count` INT NOT NULL,
    ADD COLUMN `like_average` DOUBLE NOT NULL,
    ADD COLUMN `correct_answer_average` DOUBLE NOT NULL;