-- Creating DB
CREATE DATABASE IF NOT EXISTS `booking_app` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `booking_app`;

-- Initializing Users table
CREATE TABLE IF NOT EXISTS `Users` (
  `id` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `phone_number` VARCHAR(45) NULL,
  `active` VARCHAR(45) NOT NULL,
  `created_date` DATETIME NULL,
  `updated_date` DATETIME NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC));