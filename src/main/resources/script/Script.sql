INSERT INTO user (name, email, password, role) VALUES
('Leonardo Galluzzi', 'leonardo@gmail.com', 'leo123', 'user'),
('Danilo Bonomo', 'danilo@gmail.com', 'dani123', 'admin'),
('Alessandro melotti', 'alessandro@gmail.com', 'ale123', 'user'),
('Davide Tersigni', 'davide@gmail.com', 'davide123', 'master');

CREATE DATABASE `progetto_medusa` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

-- progetto_medusa.`user` definition

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;