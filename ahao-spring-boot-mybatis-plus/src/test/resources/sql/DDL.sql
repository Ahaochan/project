DROP TABLE IF EXISTS user;

CREATE TABLE user (
id          BIGINT(20) NOT NULL COMMENT '主键ID',
username    VARCHAR(50) NOT NULL COMMENT '用户名',
email       VARCHAR(50) COMMENT '邮箱',
password    VARCHAR(50) NOT NULL COMMENT '密码',
salt        VARCHAR(50) NOT NULL COMMENT '密码加盐',
sex         TINYINT UNSIGNED DEFAULT 0 COMMENT '性别, 1男, 2女',
is_locked   TINYINT UNSIGNED DEFAULT 0 COMMENT '账户是否被锁定',
is_disabled TINYINT UNSIGNED DEFAULT 0 COMMENT '账户是否被禁用',
is_deleted  TINYINT UNSIGNED DEFAULT 0 COMMENT '账户是否被删除',
expire_time  DATETIME DEFAULT NULL COMMENT '账户过期时间',
create_by   BIGINT(20) NOT NULL COMMENT '创建人',
update_by   BIGINT(20) NOT NULL COMMENT '更新人',
create_time  DATETIME COMMENT '创建时间',
update_time  DATETIME COMMENT '更新时间',
PRIMARY KEY (id)
) DEFAULT CHARSET=utf8mb4;
