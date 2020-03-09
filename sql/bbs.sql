/*
 Navicat MySQL Data Transfer

 Source Server         : dev
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : bbs

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 09/03/2020 16:30:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for board
-- ----------------------------
DROP TABLE IF EXISTS `board`;
CREATE TABLE `board`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `board_name` varchar(90) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(600) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `enable` tinyint(0) UNSIGNED NULL DEFAULT NULL COMMENT '1启用，2弃用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of board
-- ----------------------------
INSERT INTO `board` VALUES (1, '手机', '各种手机', 1);
INSERT INTO `board` VALUES (2, '电脑', '各种电脑', 1);
INSERT INTO `board` VALUES (3, '可乐', '可口可乐\n百事可乐\n健怡可乐\n可日可乐\n天府可乐\n汾煌可乐\n崂山可乐', 1);
INSERT INTO `board` VALUES (4, '杯子', '各种杯子', 1);

-- ----------------------------
-- Table structure for reply
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(0) UNSIGNED NOT NULL COMMENT '用户id',
  `content` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `thread_id` int(0) UNSIGNED NOT NULL,
  `post_id` int(0) UNSIGNED NOT NULL COMMENT '回复其他回复的id,0是回复的帖子',
  `is_first` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是不是该贴的第一条内容',
  `is_del` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '0为未删除 1为已删除',
  `gmt_create` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reply
-- ----------------------------
INSERT INTO `reply` VALUES (1, 1, '手机手机', 1, 0, 1, 0, '2020-01-15 14:07:37', '2020-01-15 14:07:37');

-- ----------------------------
-- Table structure for reply_me
-- ----------------------------
DROP TABLE IF EXISTS `reply_me`;
CREATE TABLE `reply_me`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` int(0) UNSIGNED NOT NULL COMMENT '用户id',
  `reply_id` int(0) UNSIGNED NOT NULL COMMENT '回复id',
  `is_read` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已读，0为未读，1为已读',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for thread
-- ----------------------------
DROP TABLE IF EXISTS `thread`;
CREATE TABLE `thread`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '帖子标题',
  `user_id` int(0) UNSIGNED NOT NULL COMMENT '发帖用户id',
  `reply_count` int(0) UNSIGNED NOT NULL DEFAULT 0,
  `board_id` int(0) UNSIGNED NOT NULL,
  `is_del` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '标记是否已删除，0为未删除，1为删除',
  `gmt_create` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of thread
-- ----------------------------
INSERT INTO `thread` VALUES (1, '第一个帖子', 1, 0, 1, 0, '2020-01-15 14:07:37', '2020-01-15 14:07:37');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `email` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `tel` char(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '电话号码',
  `password` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `salt` char(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `integral` int(0) UNSIGNED NOT NULL DEFAULT 10 COMMENT '积分',
  `sex` tinyint(0) UNSIGNED NOT NULL DEFAULT 3 COMMENT '1男，2女，3保密',
  `birthday` date NOT NULL DEFAULT '1970-01-01',
  `email_public` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '电话是否公开，1公开，2保密',
  `tel_public` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '电话是否公开，1公开，2保密',
  `avatar` char(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'default.jpg' COMMENT '用户头像文件名',
  `gmt_create` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '123@qq.com', 'test', '', 'caf6676551b23a18ff2d976b2368ac07', '25ccf9f8', 10, 3, '1969-12-29', 0, 0, 'fb8365adfa3d4202867b03c5e0eeaef5482564d0.jpeg', '2020-01-15 14:06:38', '2020-03-08 15:47:24');
INSERT INTO `user` VALUES (2, '1234@qq.com', '000', '', '90e42a90301e855feead1167f5ded4b1', '308ae38a', 10, 3, '1970-01-01', 0, 0, 'default.jpg', '2020-03-06 15:31:19', '2020-03-06 15:31:19');

-- ----------------------------
-- Table structure for user_filter_chain
-- ----------------------------
DROP TABLE IF EXISTS `user_filter_chain`;
CREATE TABLE `user_filter_chain`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_filter_chain
-- ----------------------------
INSERT INTO `user_filter_chain` VALUES (1, '/**', 'user');
INSERT INTO `user_filter_chain` VALUES (2, '/board/*', 'anon');
INSERT INTO `user_filter_chain` VALUES (3, '/thread&&get', 'anon');
INSERT INTO `user_filter_chain` VALUES (4, '/thread/*', 'anon');
INSERT INTO `user_filter_chain` VALUES (5, '/lzl', 'anon');
INSERT INTO `user_filter_chain` VALUES (6, '/post', 'anon');
INSERT INTO `user_filter_chain` VALUES (7, '/account/*', 'anon');
INSERT INTO `user_filter_chain` VALUES (8, '/signup/captcha', 'anon');
INSERT INTO `user_filter_chain` VALUES (9, '/signin', 'anon');
INSERT INTO `user_filter_chain` VALUES (10, '/signup', 'anon');
INSERT INTO `user_filter_chain` VALUES (11, '/user', 'anon');
INSERT INTO `user_filter_chain` VALUES (12, '/swagger-ui.html', 'anon');
INSERT INTO `user_filter_chain` VALUES (13, '/swagger-resources/**', 'anon');
INSERT INTO `user_filter_chain` VALUES (14, '/v2/api-docs', 'anon');
INSERT INTO `user_filter_chain` VALUES (15, '/webjars/springfox-swagger-ui/**', 'anon');
INSERT INTO `user_filter_chain` VALUES (16, '/board', 'anon');
INSERT INTO `user_filter_chain` VALUES (18, '/csrf`', 'anon');
INSERT INTO `user_filter_chain` VALUES (20, '/user/password/captcha', 'anon');
INSERT INTO `user_filter_chain` VALUES (21, '/password', 'anon');
INSERT INTO `user_filter_chain` VALUES (22, '/chat', 'anon');

SET FOREIGN_KEY_CHECKS = 1;
