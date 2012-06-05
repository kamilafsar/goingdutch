# ************************************************************
# Sequel Pro SQL dump
# Version 3408
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.1.40)
# Database: goingdutch
# Generation Time: 2012-06-05 23:52:20 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table Event
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Event`;

CREATE TABLE `Event` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `owner_id` int(11) NOT NULL,
  `token` varchar(36) NOT NULL DEFAULT '',
  `isDone` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `Event` WRITE;
/*!40000 ALTER TABLE `Event` DISABLE KEYS */;

INSERT INTO `Event` (`id`, `name`, `owner_id`, `token`, `isDone`)
VALUES
	(3,'berlin',11,'f837adc4-3b54-44c5-aea1-49d0c11074a6',0);

/*!40000 ALTER TABLE `Event` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table EventUser
# ------------------------------------------------------------

DROP TABLE IF EXISTS `EventUser`;

CREATE TABLE `EventUser` (
  `event_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `EventUser` WRITE;
/*!40000 ALTER TABLE `EventUser` DISABLE KEYS */;

INSERT INTO `EventUser` (`event_id`, `user_id`)
VALUES
	(3,11),
	(3,12),
	(3,13);

/*!40000 ALTER TABLE `EventUser` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Payment
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Payment`;

CREATE TABLE `Payment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(11) NOT NULL,
  `payer_id` int(11) NOT NULL,
  `forWho` varchar(1000) NOT NULL DEFAULT '',
  `amount` double(10,2) NOT NULL,
  `description` varchar(1000) NOT NULL,
  `creationDate` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `Payment` WRITE;
/*!40000 ALTER TABLE `Payment` DISABLE KEYS */;

INSERT INTO `Payment` (`id`, `event_id`, `payer_id`, `forWho`, `amount`, `description`, `creationDate`)
VALUES
	(1,3,11,'11,12,13',10.00,'biertje','2012-06-05 13:30:43'),
	(2,3,12,'11,12,13',5.00,'biertje','2012-06-05 13:30:43'),
	(3,3,11,'11,12,13',10.00,'biertje','2012-06-05 13:31:21'),
	(4,3,12,'11,12,13',5.00,'biertje','2012-06-05 13:31:21'),
	(5,3,11,'11,12,13',7.00,'burgerking','2012-06-05 17:05:17'),
	(6,3,12,'11,12,13',10.00,'burgerking','2012-06-05 17:05:17'),
	(7,3,13,'13,12,11',100.00,'biertiets','2012-06-06 01:37:33'),
	(8,3,12,'13,12,11',10.00,'biertiets','2012-06-06 01:37:33');

/*!40000 ALTER TABLE `Payment` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table User
# ------------------------------------------------------------

DROP TABLE IF EXISTS `User`;

CREATE TABLE `User` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `token` varchar(36) NOT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT '',
  `billingInfo` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;

INSERT INTO `User` (`id`, `token`, `email`, `name`, `billingInfo`)
VALUES
	(11,'8c68ddf1-a0e7-4fa4-addf-8e06dd60e0cc','kamil@q42.nl','kamil',NULL),
	(12,'592d6d07-cd31-4eb8-b7fe-8491a36faacb','jaap@q42.nl','jaap',NULL),
	(13,'b1af6c48-b195-4d7c-8a5e-399287e99cce','martijnl@q42.nl',NULL,NULL);

/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
