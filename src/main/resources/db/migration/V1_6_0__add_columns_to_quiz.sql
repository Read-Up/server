ALTER TABLE `quiz_set`
    ADD COLUMN `participant_count` INT NOT NULL,
    ADD COLUMN `like_average` DOUBLE NOT NULL,
    ADD COLUMN `correct_answer_average` DOUBLE NOT NULL;