-- book 테이블 변경
ALTER TABLE `book`
    MODIFY COLUMN `created_by` BIGINT DEFAULT NULL,
    MODIFY COLUMN `updated_by` BIGINT DEFAULT NULL;

-- chapter 테이블 변경
ALTER TABLE `chapter`
    MODIFY COLUMN `created_by` BIGINT DEFAULT NULL,
    MODIFY COLUMN `updated_by` BIGINT DEFAULT NULL;