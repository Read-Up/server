ALTER TABLE `quiz_set`
    ADD COLUMN `estimated_time` INT NOT NULL,
    ADD COLUMN `total_quiz_count` INT NOT NULL;

ALTER TABLE `quiz`
    ADD COLUMN `sequence` INT NOT NULL;

ALTER TABLE `quiz_option`
    ADD COLUMN `sequence` INT NOT NULL;