#CREATE DATABASE invoice;
#use invoice;

CREATE TABLE invoice_user(
  id BIGINT UNSIGNED AUTO_INCREMENT COMMENT '用户ID',
  username VARCHAR(20) COMMENT '用户名',
  password VARCHAR(20) COMMENT '密码',
  last_login_time TIMESTAMP COMMENT '最后一次登录时间',
  last_login_ip VARCHAR(20) COMMENT '最后一次登录IP',
  email VARCHAR(30) COMMENT '电子邮箱',
  enabled TINYINT(1) COMMENT '账户可用',
  account_expired TINYINT(1) COMMENT '账户未过期',
  credentials_expired TINYINT(1) COMMENT '用户证书未过期',
  account_locked TINYINT(1) COMMENT '账户未上锁',
  gmt_create TIMESTAMP COMMENT '创建时间',
  gmt_modify TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (id),
  KEY idx_user_username(username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT ='用户表';

CREATE TABLE invoice_role (
  id BIGINT UNSIGNED AUTO_INCREMENT COMMENT '角色ID',
  name VARCHAR(50) COMMENT '角色名',
  description VARCHAR(100) COMMENT '角色描述',
  enabled TINYINT(1) COMMENT '角色可用',
  gmt_create TIMESTAMP COMMENT '创建时间',
  gmt_modify TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT ='角色表';

CREATE TABLE invoice_auth (
  id BIGINT UNSIGNED AUTO_INCREMENT COMMENT '权限ID',
  name VARCHAR(50) COMMENT '权限名',
  description VARCHAR(100) COMMENT '权限描述',
  enabled TINYINT(1) COMMENT '权限可用',
  gmt_create TIMESTAMP COMMENT '创建时间',
  gmt_modify TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT ='权限表';

CREATE TABLE invoice_user_role (
  id BIGINT UNSIGNED AUTO_INCREMENT COMMENT '用户角色ID',
  user_id BIGINT COMMENT '用户ID' ,
  role_id BIGINT COMMENT '角色ID',
  gmt_create TIMESTAMP COMMENT '创建时间',
  gmt_modify TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (id),
  UNIQUE INDEX idx_user_role_id (user_id,role_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT ='用户角色表';

CREATE TABLE invoice_role_auth (
  id BIGINT UNSIGNED AUTO_INCREMENT COMMENT '角色权限ID',
  role_id BIGINT COMMENT '角色ID' ,
  auth_id BIGINT COMMENT '权限ID',
  gmt_create TIMESTAMP COMMENT '创建时间',
  gmt_modify TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (id),
  UNIQUE INDEX idx_role_auth_id (role_id, auth_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT ='角色权限表';
