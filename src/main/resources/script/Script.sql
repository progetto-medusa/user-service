-- progetto_medusa.`user` definition

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `application_id` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_date` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `insert_date` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_valid` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
