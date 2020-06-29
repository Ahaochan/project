create table `table_tree` (
`id` bigint unsigned not null auto_increment comment '主键id',
`root_id` bigint unsigned not null default '0' comment '根分类id',
`parent_id` bigint unsigned not null default '0' comment '父级分类id',
`is_leaf` tinyint(1) unsigned not null default '1' comment '是否叶子节点, 0:否, 1:是',
`level` tinyint(3) unsigned not null default '1' comment '层级',
`relation_link` varchar(200) COLLATE utf8mb4_bin not null default '' comment '节点路径, 用逗号分隔',

`name` varchar(100) default NULL,

primary key (`id`) using btree,
KEY `idx_root_id` (`root_id`) using btree,
KEY `idx_parent_id` (`parent_id`) using btree,
KEY `idx_relation_link` (`relation_link`(50)) using btree
) ENGINE=InnoDB default CHARSET=utf8mb4 COLLATE=utf8mb4_bin comment='树形结构表';


#                   name1
#    name2          name3         name4
# name5  name6   name7               name8
insert into table_tree(id, root_id, parent_id, is_leaf, level, relation_link, name) values
(1, 0, 0, 0, 1, '1',        'name1'),
(2, 1, 1, 0, 2, '1,2',      'name2'),
(3, 1, 1, 0, 2, '1,3',      'name3'),
(4, 1, 1, 0, 2, '1,4',      'name4'),
(5, 1, 2, 1, 3, '1,2,5',    'name5'),
(6, 1, 2, 1, 3, '1,2,6',    'name6'),
(7, 1, 3, 1, 3, '1,3,7',    'name7'),
(8, 1, 4, 1, 3, '1,4,8',    'name8');

# 查询 id 为 2 的所有子节点
select * from table_tree where parent_id = 2;
# 查询 id 为 2 的所有子孙节点
select * from table_tree where relation_link like concat((select relation_link from table_tree where id = 2), ',%')
