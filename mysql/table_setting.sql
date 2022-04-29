use baton;

CREATE TABLE `accounts_socialuser` (
   `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
   `password` varchar(128) NOT NULL,
   `last_login` datetime(6) DEFAULT NULL,
   `is_superuser` tinyint(1) NOT NULL,
   `social_id` varchar(255) NOT NULL,
   `provider` varchar(30) NOT NULL
);

CREATE TABLE `User` (
    `id` int PRIMARY KEY NOT NULL,
    `nickname` varchar(255) NOT NULL,
    `gender` tinyint(1) DEFAULT NULL
);

CREATE TABLE `Ticket` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `seller_id` int NOT NULL,
  `location` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `price` int NOT NULL,
  `created_at` timestamp NOT NULL,
  `state` int NOT NULL,
  `type` int NOT NULL,
  `can_nego` tinyint(1) NOT NULL,
  `trade_type` int NOT NULL,
  `has_shower` tinyint(1) NOT NULL,
  `has_locker` tinyint(1) NOT NULL,
  `has_clothes` tinyint(1) NOT NULL,
  `has_gx` tinyint(1) NOT NULL,
  `can_resell` tinyint(1) NOT NULL,
  `can_refund` tinyint(1) NOT NULL,
  `is_holding` tinyint(1) NOT NULL,
  `description` varchar(255) NOT NULL,
  `transfer_pee` int NOT NULL,
  `point` point NOT NULL,
  `tag_hash` bigint NOT NULL,
  `is_membership` tinyint(1) NOT NULL,
  `expiry_date` date,
  `remaining_number` int
);

CREATE TABLE `TicketImage` (
   `id` int PRIMARY KEY AUTO_INCREMENT,
   `ticket_id` int NOT NULL,
   `url` varchar(255) NOT NULL,
   `is_main` tinyint(1)
);

CREATE TABLE `Buy` (
   `id` int PRIMARY KEY AUTO_INCREMENT,
   `user_id` int NOT NULL,
   `ticket_id` int UNIQUE NOT NULL,
   `date` datetime(0)
);

CREATE TABLE `Tag` (
   `id` int PRIMARY KEY AUTO_INCREMENT,
   `subject` varchar(255) NOT NULL,
   `content` text NOT NULL
);

CREATE TABLE `Bookmark` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `user_id` int NOT NULL,
    `ticket_id` int NOT NULL
);

CREATE TABLE `TicketTag` (
     `id` int PRIMARY KEY AUTO_INCREMENT,
     `ticket_id` int NOT NULL,
     `tag_id` int NOT NULL
);

# ALTER TABLE `User` ADD FOREIGN KEY (`id`) REFERENCES `accounts_socialuser` (`id`);

ALTER TABLE `Ticket` ADD FOREIGN KEY (`seller_id`) REFERENCES `User` (`id`);

ALTER TABLE `TicketImage` ADD FOREIGN KEY (`ticket_id`) REFERENCES `Ticket` (`id`);

ALTER TABLE `Buy` ADD FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);

ALTER TABLE `Buy` ADD FOREIGN KEY (`ticket_id`) REFERENCES `Ticket` (`id`);

ALTER TABLE `Bookmark` ADD FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);

ALTER TABLE `Bookmark` ADD FOREIGN KEY (`ticket_id`) REFERENCES `Ticket` (`id`);

ALTER TABLE `TicketTag` ADD FOREIGN KEY (`ticket_id`) REFERENCES `Ticket` (`id`);

ALTER TABLE `TicketTag` ADD FOREIGN KEY (`tag_id`) REFERENCES `Tag` (`id`);

INSERT INTO Tag (id, subject, content) VALUES (1, "태그1", "태그1"), (2, "태그2", "태그2"), (3, "태그3", "태그3");