/*
SQLyog Ultimate v10.00 Beta1
MySQL - 8.0.13 : Database - bbs
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`bbs` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `bbs`;

/*Table structure for table `board` */

DROP TABLE IF EXISTS `board`;

CREATE TABLE `board` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `board_name` varchar(90) NOT NULL,
  `description` varchar(600) NOT NULL,
  `enable` tinyint(3) unsigned DEFAULT NULL COMMENT '1启用，2弃用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

/*Data for the table `board` */

insert  into `board`(`id`,`board_name`,`description`,`enable`) values (1,'手机','各种手机',1),(2,'电脑','各种电脑',1),(3,'可乐','可口可乐\n百事可乐\n健怡可乐\n可日可乐\n天府可乐\n汾煌可乐\n崂山可乐',1),(4,'杯子','各种杯子',1);

/*Table structure for table `reply` */

DROP TABLE IF EXISTS `reply`;

CREATE TABLE `reply` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL COMMENT '用户id',
  `content` varchar(3000) NOT NULL,
  `thread_id` int(10) unsigned NOT NULL,
  `post_id` int(10) unsigned NOT NULL COMMENT '回复其他回复的id,0是回复的帖子',
  `is_first` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是不是该贴的第一条内容',
  `is_del` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '0为未删除 1为已删除',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `reply` */

insert  into `reply`(`id`,`user_id`,`content`,`thread_id`,`post_id`,`is_first`,`is_del`,`gmt_create`,`gmt_modified`) values (1,1,'手机手机',1,0,1,0,'2020-01-15 14:07:37','2020-01-15 14:07:37');

/*Table structure for table `reply_me` */

DROP TABLE IF EXISTS `reply_me`;

CREATE TABLE `reply_me` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL COMMENT '用户id',
  `reply_id` int(10) unsigned NOT NULL COMMENT '回复id',
  `is_read` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否已读，0为未读，1为已读',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `reply_me` */

/*Table structure for table `thread` */

DROP TABLE IF EXISTS `thread`;

CREATE TABLE `thread` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(150) NOT NULL COMMENT '帖子标题',
  `user_id` int(10) unsigned NOT NULL COMMENT '发帖用户id',
  `reply_count` int(11) unsigned NOT NULL DEFAULT '0',
  `board_id` int(10) unsigned NOT NULL,
  `is_del` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '标记是否已删除，0为未删除，1为删除',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `thread` */

insert  into `thread`(`id`,`title`,`user_id`,`reply_count`,`board_id`,`is_del`,`gmt_create`,`gmt_modified`) values (1,'第一个帖子',1,0,1,0,'2020-01-15 14:07:37','2020-01-15 14:07:37');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(30) NOT NULL,
  `username` varchar(30) NOT NULL,
  `tel` char(11) NOT NULL DEFAULT '' COMMENT '电话号码',
  `password` char(32) NOT NULL,
  `salt` char(8) NOT NULL,
  `integral` int(10) unsigned NOT NULL DEFAULT '10' COMMENT '积分',
  `sex` tinyint(3) unsigned NOT NULL DEFAULT '3' COMMENT '1男，2女，3保密',
  `birthday` date NOT NULL DEFAULT '1970-01-01',
  `email_public` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '电话是否公开，1公开，2保密',
  `tel_public` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '电话是否公开，1公开，2保密',
  `avatar` char(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'default.jpg' COMMENT '用户头像文件名',
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`email`,`username`,`tel`,`password`,`salt`,`integral`,`sex`,`birthday`,`email_public`,`tel_public`,`avatar`,`gmt_create`,`gmt_modified`) values (1,'123@qq.com','test','','caf6676551b23a18ff2d976b2368ac07','25ccf9f8',10,3,'1969-12-31',0,0,'25e5dd933f3c94868945d719e4d3c23e63cc4ebd.jpeg','2020-01-15 14:06:38','2020-01-15 14:22:26');

/*Table structure for table `user_filter_chain` */

DROP TABLE IF EXISTS `user_filter_chain`;

CREATE TABLE `user_filter_chain` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `path` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

/*Data for the table `user_filter_chain` */

insert  into `user_filter_chain`(`id`,`path`,`name`) values (1,'/**','user'),(2,'/board/*','anon'),(3,'/thread&&get','anon'),(4,'/thread/*','anon'),(5,'/lzl','anon'),(6,'/post','anon'),(7,'/account/*','anon'),(8,'/signup/captcha','anon'),(9,'/signin','anon'),(10,'/signup','anon'),(11,'/user','anon'),(12,'/swagger-ui.html','anon'),(13,'/swagger-resources/**','anon'),(14,'/v2/api-docs','anon'),(15,'/webjars/springfox-swagger-ui/**','anon'),(16,'/board','anon'),(18,'/csrf`','anon'),(20,'/user/password/captcha','anon'),(21,'/password','anon');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
