-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: desijeques
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `ciudades`
--

LOCK TABLES `ciudades` WRITE;
/*!40000 ALTER TABLE `ciudades` DISABLE KEYS */;
INSERT INTO `ciudades` VALUES (1,'Santa Fe',1),(2,'Esperanza',1),(3,'Santo Tome',1),(4,'Parana',2);
/*!40000 ALTER TABLE `ciudades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `contratos`
--

LOCK TABLES `contratos` WRITE;
/*!40000 ALTER TABLE `contratos` DISABLE KEYS */;
INSERT INTO `contratos` VALUES (1,'Contrato alquiler monoambiente por 24 meses.',10,24,_binary '\0','ACTIVO','2026-06-01',180000.00,4,3),(2,'Contrato borrador casa familiar.',5,12,_binary '\0','BORRADOR','2026-07-01',450000.00,5,1),(3,'Contrato finalizado departamento céntrico.',15,12,_binary '\0','FINALIZADO','2025-01-01',250000.00,6,2);
/*!40000 ALTER TABLE `contratos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `facturas`
--

LOCK TABLES `facturas` WRITE;
/*!40000 ALTER TABLE `facturas` DISABLE KEYS */;
INSERT INTO `facturas` VALUES (1,'Alquiler Junio 2026',_binary '\0','PENDIENTE','2026-06-01',NULL,'2026-06-10',180000.00,NULL,NULL,NULL,1),(2,'Alquiler Mayo 2026',_binary '\0','PAGADA','2026-05-01','2026-05-08','2026-05-10',180000.00,180000.00,0.00,'TRANSFERENCIA',1),(3,'Expensas extraordinarias',_binary '\0','VENCIDA','2026-06-15',NULL,'2026-06-20',25000.00,NULL,NULL,NULL,1),(4,'Reserva administrativa',_binary '\0','ANULADA','2026-06-02',NULL,'2026-06-05',15000.00,NULL,NULL,NULL,2);
/*!40000 ALTER TABLE `facturas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `historial_estado_contrato`
--

LOCK TABLES `historial_estado_contrato` WRITE;
/*!40000 ALTER TABLE `historial_estado_contrato` DISABLE KEYS */;
INSERT INTO `historial_estado_contrato` VALUES (1,'Activo','2026-06-27 04:01:01.000000',1),(2,'Rescindido','2026-06-27 04:04:06.000000',1),(3,'Activo','2026-06-27 04:04:30.000000',2),(4,'Rescindido','2026-06-27 04:13:33.000000',2),(5,'Activo','2026-06-30 03:35:48.000000',3);
/*!40000 ALTER TABLE `historial_estado_contrato` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `historial_estado_factura`
--

LOCK TABLES `historial_estado_factura` WRITE;
/*!40000 ALTER TABLE `historial_estado_factura` DISABLE KEYS */;
INSERT INTO `historial_estado_factura` VALUES (1,'PENDIENTE','2026-06-30 03:38:34.000000',1),(2,'PENDIENTE','2026-06-30 03:38:34.000000',2),(3,'PENDIENTE','2026-06-30 03:39:31.000000',3),(4,'PAGADA','2026-06-30 03:40:07.000000',3);
/*!40000 ALTER TABLE `historial_estado_factura` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `historial_estado_incidente`
--

LOCK TABLES `historial_estado_incidente` WRITE;
/*!40000 ALTER TABLE `historial_estado_incidente` DISABLE KEYS */;
INSERT INTO `historial_estado_incidente` VALUES (1,'ABIERTO','2026-06-05 09:00:00.000000',1),(2,'RESUELTO','2026-06-06 17:00:00.000000',1),(3,'ABIERTO','2026-06-20 14:30:00.000000',2),(4,'EN_PROCESO','2026-06-21 10:00:00.000000',2);
/*!40000 ALTER TABLE `historial_estado_incidente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `historial_estado_propiedad`
--

LOCK TABLES `historial_estado_propiedad` WRITE;
/*!40000 ALTER TABLE `historial_estado_propiedad` DISABLE KEYS */;
INSERT INTO `historial_estado_propiedad` VALUES (1,'disponible','2026-06-01 10:00:00.000000',1),(2,'reservada','2026-06-02 11:30:00.000000',2),(3,'alquilada','2026-06-03 09:15:00.000000',3),(4,'inactiva','2026-06-04 16:45:00.000000',4),(5,'disponible','2026-06-05 14:00:00.000000',5),(6,'disponible','2026-06-06 18:20:00.000000',6);
/*!40000 ALTER TABLE `historial_estado_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `historial_estado_publicacion`
--

LOCK TABLES `historial_estado_publicacion` WRITE;
/*!40000 ALTER TABLE `historial_estado_publicacion` DISABLE KEYS */;
INSERT INTO `historial_estado_publicacion` VALUES (1,'ACTIVA','2026-06-10 09:00:00.000000',1),(2,'PAUSADA','2026-06-12 10:30:00.000000',2),(3,'FINALIZADA','2026-06-20 11:00:00.000000',3),(4,'ACTIVA','2026-06-15 12:15:00.000000',4),(5,'ACTIVA','2026-06-18 13:45:00.000000',5);
/*!40000 ALTER TABLE `historial_estado_publicacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `incidentes`
--

LOCK TABLES `incidentes` WRITE;
/*!40000 ALTER TABLE `incidentes` DISABLE KEYS */;
INSERT INTO `incidentes` VALUES (1,'PLOMERIA',15000.00,'Pérdida en cañería de cocina.','RESUELTO','2026-06-05 09:00:00.000000','2026-06-06 17:00:00.000000','Se cambió flexible y se probó funcionamiento.','MEDIA','Plomería Santa Fe','Pérdida en cocina',1,1),(2,'ELECTRICIDAD',NULL,'Corte intermitente en living.','EN_PROCESO','2026-06-20 14:30:00.000000',NULL,NULL,'ALTA','Electro Servicio','Falla eléctrica',1,1);
/*!40000 ALTER TABLE `incidentes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `personas`
--

LOCK TABLES `personas` WRITE;
/*!40000 ALTER TABLE `personas` DISABLE KEYS */;
INSERT INTO `personas` VALUES (1,'30111222',_binary '\0','Alejandro Loza'),(2,'28444555',_binary '\0','Maria Gomez'),(3,'32999888',_binary '\0','Juan Perez'),(4,'30123123',_binary '\0','Lucia Fernandez'),(5,'27555444',_binary '\0','Carlos Ramirez'),(6,'33666777',_binary '\0','Sofia Martinez');
/*!40000 ALTER TABLE `personas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `propiedades`
--

LOCK TABLES `propiedades` WRITE;
/*!40000 ALTER TABLE `propiedades` DISABLE KEYS */;
INSERT INTO `propiedades` VALUES (1,3,'Santa Fe','Casa luminosa con patio y cochera.','Pje Parana 10590',_binary '\0','disponible',95,'casa',1),(2,2,'Esperanza','Departamento céntrico ideal para pareja.','Aristobulo 10500',_binary '\0','reservada',45,'departamento',2),(3,1,'Santa Fe','Monoambiente cómodo en zona universitaria.','San Martin 2200',_binary '\0','alquilada',32.5,'departamento',3),(4,4,'Santo Tome','Casa amplia con quincho.','Av. Lujan 455',_binary '\0','inactiva',120,'casa',4),(5,1,'Parana','Local comercial en galería.','Urquiza 888',_binary '\0','disponible',28,'local',5),(6,3,'Santa Fe','Departamento reciclado a nuevo.','Lisandro de la Torre 3100',_binary '\0','disponible',68,'departamento',6);
/*!40000 ALTER TABLE `propiedades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `provincias`
--

LOCK TABLES `provincias` WRITE;
/*!40000 ALTER TABLE `provincias` DISABLE KEYS */;
INSERT INTO `provincias` VALUES (1,'Santa Fe'),(2,'Entre Rios');
/*!40000 ALTER TABLE `provincias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `publicaciones`
--

LOCK TABLES `publicaciones` WRITE;
/*!40000 ALTER TABLE `publicaciones` DISABLE KEYS */;
INSERT INTO `publicaciones` VALUES (1,'Garantía propietaria o recibo de sueldo. No mascotas.','Publicación casa con patio.',_binary '\0','activa','2026-06-10',450000.00,1),(2,'Depósito de un mes. Acepta mascota pequeña.','Departamento céntrico con balcón.',_binary '\0','pausada','2026-06-12',280000.00,2),(3,'Solo recibo de sueldo. Sin mascotas.','Monoambiente equipado.',_binary '\0','finalizada','2026-06-08',180000.00,3),(4,'Garantía y depósito.','Local comercial en excelente ubicación.',_binary '\0','activa','2026-06-15',320000.00,5),(5,'Contrato anual. Sin expensas extraordinarias.','Departamento reciclado a nuevo.',_binary '\0','activa','2026-06-18',390000.00,6);
/*!40000 ALTER TABLE `publicaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `visitas`
--

LOCK TABLES `visitas` WRITE;
/*!40000 ALTER TABLE `visitas` DISABLE KEYS */;
INSERT INTO `visitas` VALUES (1,'PENDIENTE','2026-07-03 18:00:00.000000',1),(2,'REALIZADA','2026-06-25 16:30:00.000000',2),(3,'CANCELADA','2026-06-28 11:00:00.000000',4);
/*!40000 ALTER TABLE `visitas` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-30 22:00:27
