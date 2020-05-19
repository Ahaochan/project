#!/bin/bash
db_ip=`hostname -I | awk '{print $1}'`
db_port="33306"

db_url="jdbc:mysql://${db_ip}:${db_port}/xxl_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai"
db_username="root"
db_password="root"
version="2.2.0"

docker run --name mysql -d -p ${db_port}:3306 \
  --restart always \
  --privileged=true \
  -v /opt/mysql/data:/var/lib/mysql \
  -v /opt/mysql/my.cnf:/etc/my.cnf \
  -e MYSQL_ROOT_PASSWORD=root \
  mysql:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci \
  --innodb_buffer_pool_size=64M

wget "https://gitee.com/xuxueli0323/xxl-job/raw/v${version}/doc/db/tables_xxl_job.sql" -O /tmp/xxl-job.sql
mysql -h ${db_ip} -P ${db_port} -u ${db_username} -p${db_password} < /tmp/xxl-job.sql



docker run -p 8080:8080 --name xxl-job-admin -d -v /tmp:/data/applogs \
    -e PARAMS="--spring.datasource.url=${db_url}  --spring.datasource.username=${db_username}  --spring.datasource.password=${db_password}" \
    xuxueli/xxl-job-admin:${version}
