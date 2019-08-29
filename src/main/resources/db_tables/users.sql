CREATE TABLE `booking_app`.`users` (
  `id` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `phone_number` VARCHAR(45) NULL,
  `active` VARCHAR(45) NOT NULL,
  `created_date` DATETIME NULL,
  `updated_date` DATETIME NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `booking_app`.`users`
ADD UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE;
;