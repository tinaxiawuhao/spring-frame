-- MySQL dump 10.13  Distrib 5.7.38, for Win64 (x86_64)
--
-- Host: localhost    Database: security
-- ------------------------------------------------------
-- Server version	5.7.38-log

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
-- Table structure for table `sys_role_permission`
--

DROP TABLE IF EXISTS `sys_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role_permission` (
                                       `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                       `code` varchar(36) NOT NULL COMMENT '标识主键',
                                       `permission_id` int(11) DEFAULT NULL COMMENT '权限id',
                                       `role_id` int(11) DEFAULT NULL COMMENT '角色id',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       KEY `code_index` (`code`) USING BTREE COMMENT '简单索引'
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色权限关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_permission`
--

LOCK TABLES `sys_role_permission` WRITE;
/*!40000 ALTER TABLE `sys_role_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_permission`
--

DROP TABLE IF EXISTS `sys_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_permission` (
                                  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                  `code` varchar(36) NOT NULL COMMENT '标识主键',
                                  `type` int(2) DEFAULT '0' COMMENT '菜单所属系统，0平台端，1租户端',
                                  `name` varchar(64) DEFAULT NULL COMMENT '菜单名称',
                                  `description` varchar(255) DEFAULT NULL COMMENT '功能说明',
                                  `pid` int(10) DEFAULT NULL COMMENT '父id',
                                  `url` varchar(128) DEFAULT NULL COMMENT 'url路径',
                                  `menu_type` int(2) DEFAULT NULL COMMENT '菜单类型;0菜单，1按钮',
                                  `state` int(2) DEFAULT '0' COMMENT '是否可用，0可用，1禁用',
                                  `keystr` varchar(64) DEFAULT NULL COMMENT '权限标识',
                                  `routing` varchar(255) DEFAULT NULL COMMENT '前端路由',
                                  `create_by` varchar(50) DEFAULT NULL COMMENT '创建者',
                                  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_by` varchar(50) DEFAULT NULL COMMENT '修改者',
                                  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  PRIMARY KEY (`id`) USING BTREE,
                                  KEY `code_index` (`code`) USING BTREE COMMENT '简单索引'
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='权限详情';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_permission`
--

LOCK TABLES `sys_permission` WRITE;
/*!40000 ALTER TABLE `sys_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_user`
--

DROP TABLE IF EXISTS `sys_role_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role_user` (
                                 `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 `code` varchar(36) NOT NULL COMMENT '标识主键',
                                 `user_id` int(11) DEFAULT NULL COMMENT '用户id',
                                 `role_id` int(11) DEFAULT NULL COMMENT '角色id',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 KEY `code_index` (`code`) USING BTREE COMMENT '简单索引'
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户角色关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_user`
--

LOCK TABLES `sys_role_user` WRITE;
/*!40000 ALTER TABLE `sys_role_user` DISABLE KEYS */;
INSERT INTO `sys_role_user` VALUES (1,'f91bf244-67d5-4da7-ba82-c8289fca4a84',1,1);
/*!40000 ALTER TABLE `sys_role_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
                            `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                            `code` varchar(36) NOT NULL COMMENT '标识主键',
                            `username` varchar(64) DEFAULT NULL COMMENT '用户名',
                            `nick_name` varchar(64) DEFAULT NULL COMMENT '昵称',
                            `password` varchar(255) DEFAULT NULL COMMENT '密码',
                            `last_password_reset_date` datetime DEFAULT NULL COMMENT '最后一次更新密码时间',
                            `user_type` int(2) DEFAULT '0' COMMENT '用户类型，0平台管理员，1租户管理员，2租户用户',
                            `mobile` varchar(50) DEFAULT NULL COMMENT '电话号码',
                            `email` varchar(255) DEFAULT NULL COMMENT '邮件地址',
                            `sex` int(2) DEFAULT '0' COMMENT '性别，0男，1女',
                            `state` int(2) DEFAULT '0' COMMENT '是否可用，0可用，1禁用',
                            `deleted` int(2) DEFAULT '0' COMMENT '是否已删除，0未删除，1已删除',
                            `create_by` varchar(50) DEFAULT NULL COMMENT '创建者',
                            `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_by` varchar(50) DEFAULT NULL COMMENT '修改者',
                            `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `code_index` (`code`) USING BTREE COMMENT '简单索引'
) ENGINE=InnoDB AUTO_INCREMENT=209 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户详情';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'057c1d58-41a0-4c8a-850a-de1d3d755136','admin','admin','8d6b1d673f0068dc6f70d222a4d3cf33',NULL,0,'18312341234','183@163.com',0,0,0,'057c1d58-41a0-4c8a-850a-de1d3d755136','2022-11-03 05:19:38',NULL,'2022-11-03 05:19:38'),(2,'629487cb-2452-422b-9dfc-0324f40b32ba','tenant','tenant','e10adc3949ba59abbe56e057f20f883e',NULL,1,'18312341235','1831@163.com',0,0,0,'057c1d58-41a0-4c8a-850a-de1d3d755136','2022-11-03 05:32:19',NULL,'2022-11-03 05:32:52');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role` (
                            `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                            `code` varchar(36) NOT NULL COMMENT '标识主键',
                            `role` varchar(64) DEFAULT NULL COMMENT '角色名',
                            `role_description` varchar(100) DEFAULT NULL COMMENT '角色描述',
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `code_index` (`code`) USING BTREE COMMENT '简单索引'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色详情';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'83ddb71c-5b26-4cf2-aaa2-472b246cfcce','admin','超级管理员'),(2,'be1bb164-4b1f-4758-9264-da61065965d4','tenant','普通用户表');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'security'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-11-03 14:13:40
