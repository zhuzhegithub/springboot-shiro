
CREATE DATABASE /*!32312 IF NOT EXISTS*/`springboot_shiro` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `springboot_shiro`;


DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `pid` int(11) NOT NULL AUTO_INCREMENT,
  `pname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
insert  into `permissions`(`pid`,`pname`) values (1,'user:add'),(2,'user:delete'),(3,'user:query'),(4,'user:update');


DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `rid` int(11) NOT NULL AUTO_INCREMENT,
  `rname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`rid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
insert  into `role`(`rid`,`rname`) values (1,'admin');

/*Table structure for table `permissions_role` */

DROP TABLE IF EXISTS `permissions_role`;
CREATE TABLE `permissions_role` (
  `rid` int(11) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  KEY `rid` (`rid`) USING BTREE,
  KEY `pid` (`pid`) USING BTREE,
  CONSTRAINT `pid` FOREIGN KEY (`pid`) REFERENCES `permissions` (`pid`),
  CONSTRAINT `rid` FOREIGN KEY (`rid`) REFERENCES `role` (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

insert  into `permissions_role`(`rid`,`pid`) values (1,1),(1,2),(1,3);



DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

insert  into `user`(`uid`,`username`,`password`) values (1,'aaa','123');



DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `uid` int(11) DEFAULT NULL,
  `rid` int(11) DEFAULT NULL,
  KEY `u_fk` (`uid`) USING BTREE,
  KEY `r_fk` (`rid`) USING BTREE,
  CONSTRAINT `r_fk` FOREIGN KEY (`rid`) REFERENCES `role` (`rid`),
  CONSTRAINT `u_fk` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;



insert  into `user_role`(`uid`,`rid`) values (1,1);
