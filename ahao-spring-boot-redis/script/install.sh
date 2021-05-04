#!/bin/bash
# 1. 安裝依赖
apt-get install tcl

# 2. 安装 Redis
VERSION=6.2.2
wget https://download.redis.io/releases/redis-${VERSION}.tar.gz
tar xzf redis-${VERSION}.tar.gz
cd redis-${VERSION}
make

# 3. 开机时启动
PORT=6379
cp utils/redis_init_script /etc/init.d/redis_${PORT}
sed -i "s#REDISPORT=[[:digit:]]*#REDISPORT=${PORT}#g" /etc/init.d/redis_${PORT}

# 4. 配置文件
mkdir -p /etc/redis /var/redis/${PORT}
cp redis.conf /etc/redis/${PORT}.conf
sed -i "s#daemonize no#daemonize yes#g" /etc/redis/${PORT}.conf
sed -i "s#pidfile /var/run/redis_[[:digit:]]*.pid#pidfile /var/run/redis_${PORT}.pid#g" /etc/redis/${PORT}.conf
sed -i "s#port [[:digit:]]*#port ${PORT}#g" /etc/redis/${PORT}.conf
sed -i "s#^dir .*#dir /var/redis/${PORT}#g" /etc/redis/${PORT}.conf

# 5. 启动脚本
/etc/redis/redis_${PORT} start

# 6. 连接
redis-cli -h 127.0.0.1 -p ${PORT}
