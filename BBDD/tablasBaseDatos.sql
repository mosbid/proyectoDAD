-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: 127.0.0.1    Database: paco
-- ------------------------------------------------------
-- Server version	5.7.20

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
-- Table structure for table `puerta`
--

DROP TABLE IF EXISTS `puerta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `puerta` (
  `idPuerta` int(11) NOT NULL,
  `estadoPuerta` int(11) DEFAULT NULL,
  `direccionPuerta` varchar(50) DEFAULT NULL,
  `contraPuerta` varchar(200) DEFAULT NULL,
  `adminPuerta` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`idPuerta`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `puerta`
--

LOCK TABLES `puerta` WRITE;
/*!40000 ALTER TABLE `puerta` DISABLE KEYS */;
INSERT INTO `puerta` VALUES (1,3,'C Paco 22','0000','1111'),(2,2,'Reina Mercedes','2222','3333'),(3,0,'Avenida','Esto es una pass tela de guapa','Esta no lo es tanto'),(7,3,'C Paco 23','0','');
/*!40000 ALTER TABLE `puerta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `terminal`
--

DROP TABLE IF EXISTS `terminal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `terminal` (
  `idTerminal` int(11) NOT NULL,
  `acierto` tinyint(4) DEFAULT NULL,
  `fecha` int(20) DEFAULT NULL,
  `bloqueo` tinyint(4) DEFAULT NULL,
  `numIntento` int(11) DEFAULT NULL,
  `puertaRelacionada` int(11) DEFAULT NULL,
  PRIMARY KEY (`idTerminal`),
  KEY `idPuerta_idx` (`puertaRelacionada`),
  CONSTRAINT `idPuerta` FOREIGN KEY (`puertaRelacionada`) REFERENCES `puerta` (`idPuerta`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `terminal`
--

LOCK TABLES `terminal` WRITE;
/*!40000 ALTER TABLE `terminal` DISABLE KEYS */;
INSERT INTO `terminal` VALUES (2,1,1112,0,1,2),(3,1,1111,0,2,2);
/*!40000 ALTER TABLE `terminal` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-06-04 23:56:44
