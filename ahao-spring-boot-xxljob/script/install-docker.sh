#!/bin/bash
# 1. 部署 MySQL
db_ip="127.0.0.1"
db_port="3306"

db_username="root"
db_password="root"

# 2. 初始化 sql
version="2.2.0"
wget "https://gitee.com/xuxueli0323/xxl-job/raw/v${version}/doc/db/tables_xxl_job.sql" -O /tmp/xxl-job.sql
mysql -h ${db_ip} -P ${db_port} -u ${db_username} -p${db_password} < /tmp/xxl-job.sql

# 3. 部署 xxl-job
# https://hub.docker.com/r/xuxueli/xxl-job-admin/
db_url="jdbc:mysql://mysql:${db_port}/xxl_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai"
docker run --link mysql -p 8080:8080 --name xxl-job-admin -d \
    -e PARAMS=" --spring.datasource.url=${db_url}
                --spring.datasource.username=${db_username}
                --spring.datasource.password=${db_password}" \
    xuxueli/xxl-job-admin:${version}
