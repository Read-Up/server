ALTER TABLE `user`
    ADD COLUMN `image_url` varchar(255),
    ADD COLUMN `status` ENUM('PENDING','ACTIVE','DELETE');