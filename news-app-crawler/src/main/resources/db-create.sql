CREATE DATABASE news_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE news_app;
DROP TABLE IF EXISTS `news_app`;
CREATE TABLE `news_hotquery` (
  `id` bigint(11) AUTO_INCREMENT COMMENT 'primary key',
  `name` varchar(100) NOT NULL COMMENT 'name',
  `source` varchar(20) DEFAULT 'unknown' COMMENT 'baidu 360 and so on',
  `type` varchar(20) DEFAULT 'unknown' COMMENT 'news/weibo...',
  `category` varchar(20) DEFAULT 'unknown' COMMENT 'env society and so on',
  `position` int(4) DEFAULT -1 COMMENT 'original position',
  `valid` int(4) DEFAULT 0 COMMENT 'has search results',
  `create_time` DATETIME DEFAULT NOW() COMMENT 'create time stamp',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time stamp',
  PRIMARY KEY (`id`),
  UNIQUE KEY uk_name(`name`)
) DEFAULT CHARSET=utf8 COMMENT='新闻热词' ENGINE=INNODB;
