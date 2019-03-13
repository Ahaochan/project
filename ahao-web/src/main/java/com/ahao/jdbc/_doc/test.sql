CREATE TABLE `my_db1`.`sys_user`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(255) CHARACTER SET utf8 ,
PRIMARY KEY (`id`)
);
CREATE TABLE `my_db2`.`sys_user`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(255) CHARACTER SET utf8 ,
PRIMARY KEY (`id`)
);
CREATE TABLE `my_db3`.`sys_user`  (
`id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(255) CHARACTER SET utf8 ,
PRIMARY KEY (`id`)
);

insert into `my_db1`.`sys_user`(id, name) values(1,'user1');
insert into `my_db2`.`sys_user`(id, name) values(1,'user2');
insert into `my_db3`.`sys_user`(id, name) values(1,'user3');