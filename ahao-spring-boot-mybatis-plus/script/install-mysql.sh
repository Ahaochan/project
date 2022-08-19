#!/bin/bash

mkdir -p /opt/mysql/conf /opt/mysql/data /opt/mysql/logs && cd /opt/mysql
vim conf/my.cnf

docker run --name mysql -dp 3306:3306 \
  --restart always --privileged=true \
  -v $(pwd)/conf:/etc/mysql -v $(pwd)/data:/var/lib/mysql -v $(pwd)/logs:/var/log/mysql \
  -e MYSQL_ROOT_PASSWORD=root -e MYSQL_ROOT_HOST=% -e TZ=Asia/Shanghai \
  mysql:5.7 \
  --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci  --innodb_buffer_pool_size=64M

docker exec -it mysql mysql -h 127.0.0.1 -P 3306 -u root -proot -e "select now()"
