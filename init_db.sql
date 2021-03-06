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

-- Initializing Users_roles table
  CREATE TABLE IF NOT EXISTS `User_roles` (
  `id` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `created_date` DATETIME NULL,
  `updated_date` DATETIME NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC));

-- Initializing Reservations table
  CREATE TABLE IF NOT EXISTS `Reservations` (
  `id` VARCHAR(45) NOT NULL,
  `property_id` VARCHAR(45) NOT NULL,
  `user_id` VARCHAR(45) NOT NULL,
  `check_in_date` DATE NOT NULL,
  `check_out_date` DATE NOT NULL,
  `created_date` DATETIME NOT NULL,
  `updated_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`));

-- Initializing Property_infos table
  CREATE TABLE IF NOT EXISTS `Property_infos` (
  `id` VARCHAR(45) NOT NULL,
  `owner_id` VARCHAR(45) NOT NULL,
  `location` VARCHAR(90) NOT NULL,
  `description` VARCHAR(1000) NULL,
  `name` VARCHAR(45) NOT NULL,
  `created_date` DATETIME NOT NULL,
  `updated_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`));
