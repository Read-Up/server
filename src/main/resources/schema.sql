CREATE TABLE `book` (
    `id` bigint NOT NULL,
    `author` varchar(255) NOT NULL,
    `isbn` varchar(255) NOT NULL,
    `publisher` varchar(255) NOT NULL,
    `summary` varchar(255) DEFAULT NULL,
    `title` varchar(255) NOT NULL,
    `title_url` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_book_isbn` (`isbn`),
    UNIQUE KEY `uk_book_title` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `book_seq` (
    `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `chapter` (
   `chapter_number` int NOT NULL,
   `book_id` bigint NOT NULL,
   `id` bigint NOT NULL,
   `name` varchar(255) NOT NULL,
   PRIMARY KEY (`id`),
   KEY `fk_chapter_book_id` (`book_id`),
   CONSTRAINT `fk_chapter_book_id` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `chapter_seq` (
   `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci