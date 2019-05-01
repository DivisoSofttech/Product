-- MySQL dump 10.13  Distrib 5.5.23, for Win64 (x86)
--
-- Host: localhost    Database: productmicroservice
-- ------------------------------------------------------
-- Server version	5.5.23

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
-- Table structure for table `barcode`
--

DROP TABLE IF EXISTS `barcode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `barcode` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `barcode`
--

LOCK TABLES `barcode` WRITE;
/*!40000 ALTER TABLE `barcode` DISABLE KEYS */;
INSERT INTO `barcode` VALUES (1,'HR78 3510 0510 7578 7319 7','popularly used'),(2,'LT03 7688 4933 8476 0852','barcode description detail'),(3,'LT41 6524 4292 1183 9474','barcode des'),(4,'IL15 1194 8781 0796 2975 226','mostly used'),(5,'MC95 8729 9997 17EK W0CU U26L K58','barcode description'),(6,'SK41 6858 1079 4443 3321 3275','barcode description'),(7,'BR68 6180 8285 0895 3668 1538 399P L','barcode'),(8,'DE31 2340 8426 3960 9539 63','barcode description'),(9,'TN06 9199 3355 4287 6263 9769','barcode des'),(10,'AT33 7268 4142 4160 1946','barcode description');
/*!40000 ALTER TABLE `barcode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `barcode_type`
--

DROP TABLE IF EXISTS `barcode_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `barcode_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `barcode_type` varchar(255) DEFAULT NULL,
  `barcode_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_barcode_type_barcode_id` (`barcode_id`),
  CONSTRAINT `fk_barcode_type_barcode_id` FOREIGN KEY (`barcode_id`) REFERENCES `barcode` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `barcode_type`
--

LOCK TABLES `barcode_type` WRITE;
/*!40000 ALTER TABLE `barcode_type` DISABLE KEYS */;
INSERT INTO `barcode_type` VALUES (1,'Code 39',1),(2,'Code 128',3),(3,'Interleaved 2 of 5',2),(4,'Universal Product Codes (UPC)',7),(5,'International Article Number (EAN)',6),(6,'PDF417',5),(7,'Data Matrix',4),(8,'Quick Response (QR) Codes',8),(9,'code77 test',10),(10,'code 70 test',9);
/*!40000 ALTER TABLE `barcode_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `image` longblob,
  `image_content_type` varchar(255) DEFAULT NULL,
  `visible` bit(1) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'powder',NULL,NULL,'','powder stock'),(2,'chocolate',NULL,NULL,'','chocolatess'),(3,'drinks',NULL,NULL,'','drinks'),(4,'vegetables',NULL,NULL,'','vegee'),(5,'categoryy',NULL,NULL,'','category type');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `databasechangelog`
--

DROP TABLE IF EXISTS `databasechangelog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `databasechangelog` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `databasechangelog`
--

LOCK TABLES `databasechangelog` WRITE;
/*!40000 ALTER TABLE `databasechangelog` DISABLE KEYS */;
INSERT INTO `databasechangelog` VALUES ('00000000000001','jhipster','config/liquibase/changelog/00000000000000_initial_schema.xml','2019-04-30 11:56:15',1,'EXECUTED','7:afe878fb30103f4b02982621400de20e','createTable tableName=jhi_user; createTable tableName=jhi_authority; createTable tableName=jhi_user_authority; addPrimaryKey tableName=jhi_user_authority; addForeignKeyConstraint baseTableName=jhi_user_authority, constraintName=fk_authority_name, ...','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072408-1','jhipster','config/liquibase/changelog/20190417072408_added_entity_Product.xml','2019-04-30 11:56:16',2,'EXECUTED','7:ee038fc764c7c670b35f49280a6bddc7','createTable tableName=product; createTable tableName=product_labels; addPrimaryKey tableName=product_labels; createTable tableName=product_category; addPrimaryKey tableName=product_category','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072409-1','jhipster','config/liquibase/changelog/20190417072409_added_entity_Barcode.xml','2019-04-30 11:56:16',3,'EXECUTED','7:520fb664fefa6f991d219d1c936b3ddc','createTable tableName=barcode','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072410-1','jhipster','config/liquibase/changelog/20190417072410_added_entity_BarcodeType.xml','2019-04-30 11:56:16',4,'EXECUTED','7:6ec04e432ec628001ebaef5b43765fe2','createTable tableName=barcode_type','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072411-1','jhipster','config/liquibase/changelog/20190417072411_added_entity_Uom.xml','2019-04-30 11:56:16',5,'EXECUTED','7:9a519a9e1235dde3063ef61b0c223a30','createTable tableName=uom','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072412-1','jhipster','config/liquibase/changelog/20190417072412_added_entity_Category.xml','2019-04-30 11:56:16',6,'EXECUTED','7:f7ad7e58d8a63431686f16f74afac7b3','createTable tableName=category','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072413-1','jhipster','config/liquibase/changelog/20190417072413_added_entity_Label.xml','2019-04-30 11:56:16',7,'EXECUTED','7:04b5134f941f7505e21560cfb54bc127','createTable tableName=label','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072414-1','jhipster','config/liquibase/changelog/20190417072414_added_entity_Note.xml','2019-04-30 11:56:16',8,'EXECUTED','7:1751a9a4c4974b24888815bd24824225','createTable tableName=note','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072415-1','jhipster','config/liquibase/changelog/20190417072415_added_entity_Status.xml','2019-04-30 11:56:17',9,'EXECUTED','7:b78e7bd7773828984b5bfd1daab1e253','createTable tableName=status','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072416-1','jhipster','config/liquibase/changelog/20190417072416_added_entity_Stock.xml','2019-04-30 11:56:17',10,'EXECUTED','7:ca3e930fbeb8ebca7ff60d712a45e16e','createTable tableName=stock; createTable tableName=stock_stock_lines; addPrimaryKey tableName=stock_stock_lines','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072417-1','jhipster','config/liquibase/changelog/20190417072417_added_entity_StockLine.xml','2019-04-30 11:56:17',11,'EXECUTED','7:7d13dabce36043616c89031049a766fc','createTable tableName=stock_line','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072418-1','jhipster','config/liquibase/changelog/20190417072418_added_entity_TaxCategory.xml','2019-04-30 11:56:17',12,'EXECUTED','7:7905fe30d26f71959ac16998b24f0ce7','createTable tableName=tax_category','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072419-1','jhipster','config/liquibase/changelog/20190417072419_added_entity_Tax.xml','2019-04-30 11:56:18',13,'EXECUTED','7:3718a35fbd165102fc0ed9aca7b72e24','createTable tableName=tax','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072420-1','jhipster','config/liquibase/changelog/20190417072420_added_entity_TaxType.xml','2019-04-30 11:56:18',14,'EXECUTED','7:efa75588aaa0b10ad563fc46341aabd5','createTable tableName=tax_type','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072408-2','jhipster','config/liquibase/changelog/20190417072408_added_entity_constraints_Product.xml','2019-04-30 11:56:21',15,'EXECUTED','7:a256a5b5e8ce21da74e9562b022cbead','addForeignKeyConstraint baseTableName=product, constraintName=fk_product_barcode_id, referencedTableName=barcode; addForeignKeyConstraint baseTableName=product_labels, constraintName=fk_product_labels_product_id, referencedTableName=product; addFo...','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072410-2','jhipster','config/liquibase/changelog/20190417072410_added_entity_constraints_BarcodeType.xml','2019-04-30 11:56:21',16,'EXECUTED','7:d7af06b57418e08684ad5fbf7dee1096','addForeignKeyConstraint baseTableName=barcode_type, constraintName=fk_barcode_type_barcode_id, referencedTableName=barcode','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072414-2','jhipster','config/liquibase/changelog/20190417072414_added_entity_constraints_Note.xml','2019-04-30 11:56:21',17,'EXECUTED','7:f2d98bf7c34524ee0bcf4cd4a7c0088f','addForeignKeyConstraint baseTableName=note, constraintName=fk_note_product_id, referencedTableName=product','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072416-2','jhipster','config/liquibase/changelog/20190417072416_added_entity_constraints_Stock.xml','2019-04-30 11:56:22',18,'EXECUTED','7:5a2a5e2e6cf7a15ccee57f5711aced1f','addForeignKeyConstraint baseTableName=stock, constraintName=fk_stock_status_id, referencedTableName=status; addForeignKeyConstraint baseTableName=stock_stock_lines, constraintName=fk_stock_stock_lines_stock_id, referencedTableName=stock; addForeig...','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072417-2','jhipster','config/liquibase/changelog/20190417072417_added_entity_constraints_StockLine.xml','2019-04-30 11:56:22',19,'EXECUTED','7:bc3304d9a0144dbae4de72cc1b807ad9','addForeignKeyConstraint baseTableName=stock_line, constraintName=fk_stock_line_product_id, referencedTableName=product; addForeignKeyConstraint baseTableName=stock_line, constraintName=fk_stock_line_uom_id, referencedTableName=uom','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072419-2','jhipster','config/liquibase/changelog/20190417072419_added_entity_constraints_Tax.xml','2019-04-30 11:56:23',20,'EXECUTED','7:24e27aa5f6d9006efe8f3cafe6324e93','addForeignKeyConstraint baseTableName=tax, constraintName=fk_tax_tax_category_id, referencedTableName=tax_category','',NULL,'3.5.4',NULL,NULL,'6605572394'),('20190417072420-2','jhipster','config/liquibase/changelog/20190417072420_added_entity_constraints_TaxType.xml','2019-04-30 11:56:23',21,'EXECUTED','7:2612b76f7d32aed96332cde917ddf5af','addForeignKeyConstraint baseTableName=tax_type, constraintName=fk_tax_type_tax_id, referencedTableName=tax','',NULL,'3.5.4',NULL,NULL,'6605572394');
/*!40000 ALTER TABLE `databasechangelog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `databasechangeloglock`
--

DROP TABLE IF EXISTS `databasechangeloglock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `databasechangeloglock`
--

LOCK TABLES `databasechangeloglock` WRITE;
/*!40000 ALTER TABLE `databasechangeloglock` DISABLE KEYS */;
INSERT INTO `databasechangeloglock` VALUES (1,'\0',NULL,NULL);
/*!40000 ALTER TABLE `databasechangeloglock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_authority`
--

DROP TABLE IF EXISTS `jhi_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jhi_authority` (
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_authority`
--

LOCK TABLES `jhi_authority` WRITE;
/*!40000 ALTER TABLE `jhi_authority` DISABLE KEYS */;
/*!40000 ALTER TABLE `jhi_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_persistent_audit_event`
--

DROP TABLE IF EXISTS `jhi_persistent_audit_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jhi_persistent_audit_event` (
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `principal` varchar(50) NOT NULL,
  `event_date` timestamp NULL DEFAULT NULL,
  `event_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  KEY `idx_persistent_audit_event` (`principal`,`event_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_persistent_audit_event`
--

LOCK TABLES `jhi_persistent_audit_event` WRITE;
/*!40000 ALTER TABLE `jhi_persistent_audit_event` DISABLE KEYS */;
/*!40000 ALTER TABLE `jhi_persistent_audit_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_persistent_audit_evt_data`
--

DROP TABLE IF EXISTS `jhi_persistent_audit_evt_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jhi_persistent_audit_evt_data` (
  `event_id` bigint(20) NOT NULL,
  `name` varchar(150) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`event_id`,`name`),
  KEY `idx_persistent_audit_evt_data` (`event_id`),
  CONSTRAINT `fk_evt_pers_audit_evt_data` FOREIGN KEY (`event_id`) REFERENCES `jhi_persistent_audit_event` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_persistent_audit_evt_data`
--

LOCK TABLES `jhi_persistent_audit_evt_data` WRITE;
/*!40000 ALTER TABLE `jhi_persistent_audit_evt_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `jhi_persistent_audit_evt_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_user`
--

DROP TABLE IF EXISTS `jhi_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jhi_user` (
  `id` varchar(100) NOT NULL,
  `login` varchar(50) NOT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `email` varchar(191) DEFAULT NULL,
  `image_url` varchar(256) DEFAULT NULL,
  `activated` bit(1) NOT NULL,
  `lang_key` varchar(6) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_user_login` (`login`),
  UNIQUE KEY `ux_user_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_user`
--

LOCK TABLES `jhi_user` WRITE;
/*!40000 ALTER TABLE `jhi_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `jhi_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_user_authority`
--

DROP TABLE IF EXISTS `jhi_user_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jhi_user_authority` (
  `user_id` varchar(100) NOT NULL,
  `authority_name` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`,`authority_name`),
  KEY `fk_authority_name` (`authority_name`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`),
  CONSTRAINT `fk_authority_name` FOREIGN KEY (`authority_name`) REFERENCES `jhi_authority` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_user_authority`
--

LOCK TABLES `jhi_user_authority` WRITE;
/*!40000 ALTER TABLE `jhi_user_authority` DISABLE KEYS */;
/*!40000 ALTER TABLE `jhi_user_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `label`
--

DROP TABLE IF EXISTS `label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `label` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `label`
--

LOCK TABLES `label` WRITE;
/*!40000 ALTER TABLE `label` DISABLE KEYS */;
INSERT INTO `label` VALUES (1,'Cheese Co','Chocolate company'),(2,'Frootyy','frooty yummy'),(3,'layss','layss bites'),(4,'good Popcorn','spicey popcon'),(5,'spicy','spicy potatoes'),(6,'choco','chocolate shake'),(7,'yummy chocolate','chocoo'),(8,'peanut butter','yummy butter'),(9,'fresh sauce','spicy sauce'),(10,'ketchup','ketchup yumy');
/*!40000 ALTER TABLE `label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `note`
--

DROP TABLE IF EXISTS `note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `note` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `matter` varchar(255) DEFAULT NULL,
  `date_of_creation` date DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_note_product_id` (`product_id`),
  CONSTRAINT `fk_note_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `note`
--

LOCK TABLES `note` WRITE;
/*!40000 ALTER TABLE `note` DISABLE KEYS */;
INSERT INTO `note` VALUES (1,'product notee','2019-05-10',1),(2,'this product is good','2019-05-01',2),(3,'this product is fine','2019-05-10',3),(4,'good','2019-05-05',4),(5,'nice','2019-05-20',5),(6,'this product is excellent','2019-05-10',6),(7,'wow','2019-05-10',7),(8,'super','2019-05-10',8);
/*!40000 ALTER TABLE `note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reference` varchar(255) NOT NULL,
  `searchkey` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `image` longblob,
  `image_content_type` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `sku` varchar(255) DEFAULT NULL,
  `mpn` varchar(255) DEFAULT NULL,
  `visible` bit(1) DEFAULT NULL,
  `date_of_mfd` date DEFAULT NULL,
  `date_of_expiry` date DEFAULT NULL,
  `maximum_stock_level` double DEFAULT NULL,
  `re_order_level` double DEFAULT NULL,
  `barcode_id` bigint(20) DEFAULT NULL,
  `status_id` bigint(20) DEFAULT NULL,
  `tax_category_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_product_barcode_id` (`barcode_id`),
  KEY `fk_product_status_id` (`status_id`),
  KEY `fk_product_tax_category_id` (`tax_category_id`),
  CONSTRAINT `fk_product_tax_category_id` FOREIGN KEY (`tax_category_id`) REFERENCES `tax_category` (`id`),
  CONSTRAINT `fk_product_barcode_id` FOREIGN KEY (`barcode_id`) REFERENCES `barcode` (`id`),
  CONSTRAINT `fk_product_status_id` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'cheeeesee','cheeeesee ref','cheese',NULL,NULL,'cheeeesee description','sku description','mpn value','','2019-05-01','2020-05-01',100,20,1,1,3),(2,'frooty','frooty ref','frooty',NULL,NULL,'frooty description','sku description','mpn value','','2019-04-01','2020-07-01',50,10,5,1,1),(3,'product3','product3 ref','product3',NULL,NULL,'product3 description','sku description','mpn value','','2018-05-01','2021-05-01',100,20,2,2,2),(4,'product4','product4 ref','product4',NULL,NULL,'product4 description','sku description','mpn value','','2019-03-10','2022-05-10',50,10,9,4,1),(5,'product5','product5 ref','product5',NULL,NULL,'product5 description','sku description','mpn value','\0','2019-05-20','2020-05-01',100,20,7,4,2),(6,'product6','product6 ref','product6',NULL,NULL,'product6 description','sku description','mpn value','\0','2019-05-01','2020-07-01',100,20,6,1,2),(7,'product7','product7 ref','product7',NULL,NULL,'product7 description','sku description','mpn value','','2019-03-01','2021-05-01',100,20,8,1,4),(8,'product8','product8 ref','product8',NULL,NULL,'product8 description','sku description','mpn value','\0','2019-01-01','2021-05-01',100,20,10,1,2);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_category`
--

DROP TABLE IF EXISTS `product_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_category` (
  `category_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  PRIMARY KEY (`product_id`,`category_id`),
  KEY `fk_product_category_category_id` (`category_id`),
  CONSTRAINT `fk_product_category_category_id` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `fk_product_category_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_category`
--

LOCK TABLES `product_category` WRITE;
/*!40000 ALTER TABLE `product_category` DISABLE KEYS */;
INSERT INTO `product_category` VALUES (3,2),(3,4),(4,2),(5,1);
/*!40000 ALTER TABLE `product_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_labels`
--

DROP TABLE IF EXISTS `product_labels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_labels` (
  `labels_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  PRIMARY KEY (`product_id`,`labels_id`),
  KEY `fk_product_labels_labels_id` (`labels_id`),
  CONSTRAINT `fk_product_labels_labels_id` FOREIGN KEY (`labels_id`) REFERENCES `label` (`id`),
  CONSTRAINT `fk_product_labels_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_labels`
--

LOCK TABLES `product_labels` WRITE;
/*!40000 ALTER TABLE `product_labels` DISABLE KEYS */;
INSERT INTO `product_labels` VALUES (1,1),(1,4),(2,2),(2,7),(4,8),(6,5),(8,3),(9,6);
/*!40000 ALTER TABLE `product_labels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reference` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (1,'in stock','in stock','available'),(2,'not available','not available','not available'),(3,'very few left','very few left','very few left'),(4,'running out of stock','running out of stock','running out of stock');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock`
--

DROP TABLE IF EXISTS `stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reference` varchar(255) NOT NULL,
  `delivery_note_ref` bigint(20) DEFAULT NULL,
  `date_of_stock_updated` date DEFAULT NULL,
  `storage_cost` double DEFAULT NULL,
  `status_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_stock_status_id` (`status_id`),
  CONSTRAINT `fk_stock_status_id` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock`
--

LOCK TABLES `stock` WRITE;
/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
INSERT INTO `stock` VALUES (1,'stock reference',1,'2019-04-01',10,1),(2,'stock reference',2,'2019-04-01',20,3),(3,'stock reference',3,'2019-04-01',10,2),(4,'stock reference',4,'2019-04-01',30,1),(5,'stock reference',5,'2019-04-01',10,4),(6,'stock reference',6,'2019-04-01',20,1),(7,'stock reference',7,'2019-04-01',10,4),(8,'stock reference',8,'2019-04-01',10,1);
/*!40000 ALTER TABLE `stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_line`
--

DROP TABLE IF EXISTS `stock_line`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stock_line` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reference` varchar(255) NOT NULL,
  `buy_price` double DEFAULT NULL,
  `sell_price_inclusive` double DEFAULT NULL,
  `sell_price_exclusive` double DEFAULT NULL,
  `gross_profit` double DEFAULT NULL,
  `margin` double DEFAULT NULL,
  `units` double NOT NULL,
  `infrastructure_id` bigint(20) DEFAULT NULL,
  `location_id` varchar(255) DEFAULT NULL,
  `supplier_ref` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `uom_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_stock_line_product_id` (`product_id`),
  KEY `fk_stock_line_uom_id` (`uom_id`),
  CONSTRAINT `fk_stock_line_uom_id` FOREIGN KEY (`uom_id`) REFERENCES `uom` (`id`),
  CONSTRAINT `fk_stock_line_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_line`
--

LOCK TABLES `stock_line` WRITE;
/*!40000 ALTER TABLE `stock_line` DISABLE KEYS */;
INSERT INTO `stock_line` VALUES (1,'imperdiet nullam',75,80,5,13,100,59,92,'massa quis',86,1,2),(2,'suspendisse potenti',14,69,73,11,67,75,85,'odio donec',4,3,1),(3,'est congue',65,63,65,90,29,50,99,'accumsan tortor',56,4,3),(4,'proin at',49,16,38,74,33,33,29,'eros elementum',26,5,4),(5,'interdum mauris',45,19,44,38,69,91,32,'rutrum rutrum',16,2,5),(6,'mauris sit',10,37,7,90,36,98,71,'tellus semper',80,6,6),(7,'tincidunt in',80,47,34,12,26,78,20,'eget eros',99,7,8),(8,'aliquet massa',25,94,80,94,57,73,77,'orci luctus',7,8,7);
/*!40000 ALTER TABLE `stock_line` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_stock_lines`
--

DROP TABLE IF EXISTS `stock_stock_lines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stock_stock_lines` (
  `stock_lines_id` bigint(20) NOT NULL,
  `stock_id` bigint(20) NOT NULL,
  PRIMARY KEY (`stock_id`,`stock_lines_id`),
  KEY `fk_stock_stock_lines_stock_lines_id` (`stock_lines_id`),
  CONSTRAINT `fk_stock_stock_lines_stock_lines_id` FOREIGN KEY (`stock_lines_id`) REFERENCES `stock_line` (`id`),
  CONSTRAINT `fk_stock_stock_lines_stock_id` FOREIGN KEY (`stock_id`) REFERENCES `stock` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_stock_lines`
--

LOCK TABLES `stock_stock_lines` WRITE;
/*!40000 ALTER TABLE `stock_stock_lines` DISABLE KEYS */;
INSERT INTO `stock_stock_lines` VALUES (1,5),(2,3),(3,4),(4,8),(5,1),(6,7),(7,6),(8,2);
/*!40000 ALTER TABLE `stock_stock_lines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tax`
--

DROP TABLE IF EXISTS `tax`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tax` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `rate` double NOT NULL,
  `tax_category_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tax_tax_category_id` (`tax_category_id`),
  CONSTRAINT `fk_tax_tax_category_id` FOREIGN KEY (`tax_category_id`) REFERENCES `tax_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tax`
--

LOCK TABLES `tax` WRITE;
/*!40000 ALTER TABLE `tax` DISABLE KEYS */;
INSERT INTO `tax` VALUES (1,'taxx','tax description',10,2),(2,'tax1','tax description',7,1),(3,'tax2','tax description',5,3),(4,'tax3','tax description',12,4),(5,'tax4','tax description',6,1),(6,'tax5','tax description',7,2),(7,'tax6','tax description',10,3),(8,'tax7','tax description',11,4);
/*!40000 ALTER TABLE `tax` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tax_category`
--

DROP TABLE IF EXISTS `tax_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tax_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tax_category`
--

LOCK TABLES `tax_category` WRITE;
/*!40000 ALTER TABLE `tax_category` DISABLE KEYS */;
INSERT INTO `tax_category` VALUES (1,'No tax','No tax for the item'),(2,'1% tax','1% tax for the item'),(3,'4% tax','4% tax for the item'),(4,'full tax','full tax for the item');
/*!40000 ALTER TABLE `tax_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tax_type`
--

DROP TABLE IF EXISTS `tax_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tax_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tax_type` varchar(255) DEFAULT NULL,
  `tax_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tax_type_tax_id` (`tax_id`),
  CONSTRAINT `fk_tax_type_tax_id` FOREIGN KEY (`tax_id`) REFERENCES `tax` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tax_type`
--

LOCK TABLES `tax_type` WRITE;
/*!40000 ALTER TABLE `tax_type` DISABLE KEYS */;
INSERT INTO `tax_type` VALUES (1,'service',2),(2,'custom',1),(3,'excise duty',4),(4,'VAT',3),(5,'SIT',3),(6,'STAMP DUTY',3),(7,'tax1',3);
/*!40000 ALTER TABLE `tax_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uom`
--

DROP TABLE IF EXISTS `uom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uom` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uom`
--

LOCK TABLES `uom` WRITE;
/*!40000 ALTER TABLE `uom` DISABLE KEYS */;
INSERT INTO `uom` VALUES (1,'uom name','uom description'),(2,'uom name','uom description'),(3,'uom name','uom description'),(4,'uom name','uom description'),(5,'uom name','uom description'),(6,'uom name','uom description'),(7,'uom name','uom description'),(8,'uom name','uom description');
/*!40000 ALTER TABLE `uom` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-04-30 16:07:49
