-- MySQL dump 10.14  Distrib 5.5.52-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: dyn_server_db
-- ------------------------------------------------------
-- Server version       5.5.52-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `dyn_dns_records`
--

DROP TABLE IF EXISTS `dyn_dns_records`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dyn_dns_records` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `zone` varchar(255) NOT NULL,
  `host` varchar(255) NOT NULL DEFAULT '@',
  `type` varchar(255) NOT NULL,
  `data` text,
  `ttl` int(11) DEFAULT NULL,
  `mx_priority` int(11) DEFAULT NULL,
  `refresh` int(11) DEFAULT NULL,
  `retry` int(11) DEFAULT NULL,
  `expire` int(11) DEFAULT NULL,
  `minimum` int(11) DEFAULT NULL,
  `serial` bigint(20) DEFAULT NULL,
  `resp_person` varchar(255) DEFAULT NULL,
  `primary_ns` varchar(255) DEFAULT NULL,
  `hostID` int(11) DEFAULT NULL,
  `domainID` int(11) DEFAULT NULL,
  `userID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `type` (`type`),
  KEY `host` (`host`),
  KEY `zone` (`zone`),
  KEY `zone_host_index` (`zone`(30),`host`(30)),
  KEY `type_index` (`type`(8))
) ENGINE=InnoDB AUTO_INCREMENT=270 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dyn_dns_records`
--

LOCK TABLES `dyn_dns_records` WRITE;
/*!40000 ALTER TABLE `dyn_dns_records` DISABLE KEYS */;
INSERT INTO `dyn_dns_records` VALUES (0,'example.com','@','A','66.183.166.57',180,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,50,NULL),(224,'example.com','@','SOA',NULL,180,NULL,10800,7200,604800,86400,1411491104,'support','ns1',NULL,50,NULL),(225,'example.com','@','NS','ns1',180,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,50,NULL),(226,'example.com','@','NS','ns2',180,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,50,NULL),(228,'example.com','ns1','A','66.183.166.126',180,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,50,NULL),(229,'example.com','ns2','A','66.183.166.67',180,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,50,NULL),(230,'example.com','www','A','66.183.166.57',180,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,50,NULL),(269,'parkie.io','testerpdf','A','5.189.180.201',180,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,50,1);
/*!40000 ALTER TABLE `dyn_dns_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyn_domains`
--

DROP TABLE IF EXISTS `dyn_domains`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dyn_domains` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `domain_name` varchar(100) NOT NULL,
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `domain_name_UNIQUE` (`domain_name`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dyn_domains`
--

LOCK TABLES `dyn_domains` WRITE;
/*!40000 ALTER TABLE `dyn_domains` DISABLE KEYS */;
INSERT INTO `dyn_domains` VALUES (50,'example.com','2017-04-10 11:20:39');
/*!40000 ALTER TABLE `dyn_domains` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyn_hosts`
--

DROP TABLE IF EXISTS `dyn_hosts`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dyn_hosts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hostname` varchar(45) NOT NULL,
  `domain_id` int(11) NOT NULL,
  `ip_address` varchar(15) DEFAULT '127.0.0.1',
  `last_update` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `dyn_user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dyn_hosts`
--

LOCK TABLES `dyn_hosts` WRITE;
/*!40000 ALTER TABLE `dyn_hosts` DISABLE KEYS */;
INSERT INTO `dyn_hosts` VALUES (1,'test',50,'66.183.166.67','2017-04-16 03:27:55',1);
/*!40000 ALTER TABLE `dyn_hosts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyn_properties`
--

DROP TABLE IF EXISTS `dyn_properties`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dyn_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ttl` int(11) DEFAULT '180' COMMENT 'Min Value: 1\nMax Value: 604800 ',
  `refresh` int(11) DEFAULT '10800' COMMENT 'Min Value: 1800\nMax Value: 86400 \nDefault: 7200',
  `expire` int(11) DEFAULT '604800' COMMENT 'Min Value: 1\nMax Value: 604800 ',
  `retry` int(11) DEFAULT '7200' COMMENT 'Min Value: 600\nMax Value: 86400\nDefault: 900',
  `minimum` int(11) DEFAULT '86400' COMMENT 'Min Value: 300\nMax Value: 86400\nDefault: 86400',
  `serial` int(11) DEFAULT NULL,
  `resp_person` varchar(100) DEFAULT 'support' COMMENT 'just hostname of email address hostname@email.com',
  `primary_ns` varchar(45) DEFAULT '172.16.254.100' COMMENT 'IP Address of NS',
  `secondary_ns` varchar(45) DEFAULT '172.16.254.113' COMMENT 'IP Address of NS',
  `root_domain_ip` varchar(45) DEFAULT '172.16.254.113' COMMENT 'Root IP Address, Where the domain is hosted',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dyn_properties`
--

LOCK TABLES `dyn_properties` WRITE;
/*!40000 ALTER TABLE `dyn_properties` DISABLE KEYS */;
INSERT INTO `dyn_properties` VALUES (1,180,10800,1209600,180,10800,2017041401,'support','66.183.166.126','66.183.166.67','66.183.166.57');
/*!40000 ALTER TABLE `dyn_properties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyn_user_roles`
--

DROP TABLE IF EXISTS `dyn_user_roles`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dyn_user_roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(45) DEFAULT NULL,
  `role_name` varchar(45) DEFAULT 'dynusers',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dyn_user_roles`
--

LOCK TABLES `dyn_user_roles` WRITE;
/*!40000 ALTER TABLE `dyn_user_roles` DISABLE KEYS */;
INSERT INTO `dyn_user_roles` VALUES (1,'admin','dynusers'),(8,'admin','manager-gui');
/*!40000 ALTER TABLE `dyn_user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dyn_users`
--

DROP TABLE IF EXISTS `dyn_users`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dyn_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(65) NOT NULL,
  `user` varchar(50) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `enabled` int(1) DEFAULT '0',
  `key1` varchar(255) DEFAULT NULL,
  `key2` varchar(255) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `admin` int(1) DEFAULT '0',
  `forgot` int(1) DEFAULT '0',
  `forgot_date` timestamp NULL DEFAULT NULL,
  `enable_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`,`user`,`email`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `user_UNIQUE` (`user`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dyn_users`
--

LOCK TABLES `dyn_users` WRITE;
/*!40000 ALTER TABLE `dyn_users` DISABLE KEYS */;
INSERT INTO `dyn_users` VALUES (1,'user@email.com','admin','5f4dcc3b5aa765d61d8327deb882cf99',1,'m378jpc77q6sn8rgjm2rrt94iu6dbfsbsr8d6qo59pvlvierdi6u4c1gmnpvluj4a8i5flslne3q','9gh0tf4ub4hs9ofujldv84at6bftg5nl2ock27knnq9b0mavfiq4k3e9fro14fpp3fa7npb32hjt3','2017-04-02 16:42:04',1,0,'2017-04-15 23:49:53','2017-04-07 23:33:18');
/*!40000 ALTER TABLE `dyn_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xfr_table`
--

DROP TABLE IF EXISTS `xfr_table`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `xfr_table` (
  `zone` varchar(255) NOT NULL,
  `client` varchar(255) NOT NULL,
  KEY `zone` (`zone`),
  KEY `client` (`client`),
  KEY `zone_client_index` (`zone`(30),`client`(30))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xfr_table`
--

LOCK TABLES `xfr_table` WRITE;
/*!40000 ALTER TABLE `xfr_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `xfr_table` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-13 20:25:30