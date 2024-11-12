CREATE DATABASE AcademicAppDB;

USE AcademicAppDB;

DROP TABLE IF EXISTS `utilisateur`;
DROP TABLE IF EXISTS `etudiant`;
DROP TABLE IF EXISTS `cours`;
DROP TABLE IF EXISTS `enseignant`;
DROP TABLE IF EXISTS `inscription`;
DROP TABLE IF EXISTS `resultat`;

CREATE TABLE `utilisateur` (
	`id` int NOT NULL AUTO_INCREMENT,
    `email` varchar(100) NOT NULL,
	`password` varchar(255) NOT NULL,
    `role` enum('etudiant','enseignant','admin') NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email` (`email`)
);

CREATE TABLE `etudiant` (
  `id` int NOT NULL AUTO_INCREMENT,
  `utilisateur_id` int NOT NULL,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `date_naissance` date DEFAULT NULL,
  `contact` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `utilisateur_id` (`utilisateur_id`),
  CONSTRAINT `etudiant_ibfk_1` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE
);

CREATE TABLE `enseignant` (
  `id` int NOT NULL AUTO_INCREMENT,
  `utilisateur_id` int NOT NULL,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `contact` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `utilisateur_id` (`utilisateur_id`),
  CONSTRAINT `enseignant_ibfk_1` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE
);

CREATE TABLE `cours` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom_cours` varchar(100) NOT NULL,
  `description` text,
  `enseignant_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `enseignant_id` (`enseignant_id`),
  CONSTRAINT `cours_ibfk_1` FOREIGN KEY (`enseignant_id`) REFERENCES `enseignant` (`id`) ON DELETE SET NULL
);

CREATE TABLE `inscription` (
  `id` int NOT NULL AUTO_INCREMENT,
  `etudiant_id` int NOT NULL,
  `cours_id` int NOT NULL,
  `date_inscription` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `etudiant_id` (`etudiant_id`),
  KEY `cours_id` (`cours_id`),
  CONSTRAINT `inscription_ibfk_1` FOREIGN KEY (`etudiant_id`) REFERENCES `etudiant` (`id`) ON DELETE CASCADE,
  CONSTRAINT `inscription_ibfk_2` FOREIGN KEY (`cours_id`) REFERENCES `cours` (`id`) ON DELETE CASCADE
);

CREATE TABLE `resultat` (
  `id` int NOT NULL AUTO_INCREMENT,
  `inscription_id` int NOT NULL,
  `note` decimal(5,2) DEFAULT NULL,
  `date_saisie` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `inscription_id` (`inscription_id`),
  CONSTRAINT `resultat_ibfk_1` FOREIGN KEY (`inscription_id`) REFERENCES `inscription` (`id`) ON DELETE CASCADE
);