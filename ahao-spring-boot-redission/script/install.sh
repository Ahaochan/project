#!/bin/bash
# 1. 安裝依赖
apt-get install tcl make gcc -y

# 2. 安装 Redis
VERSION=6.2.2
wget https://download.redis.io/releases/redis-${VERSION}.tar.gz
tar xzf redis-${VERSION}.tar.gz
cd redis-${VERSION}
make distclean && make && make test && make install

# 3. 开机时启动
PORT=6379
cp utils/redis_init_script /etc/init.d/redis_${PORT}
sed -i "s#REDISPORT=[[:digit:]]*#REDISPORT=${PORT}#g" /etc/init.d/redis_${PORT}

# 4. 配置文件
mkdir -p /etc/redis /var/redis/${PORT}
cp redis.conf /etc/redis/${PORT}.conf
sed -i "s#^daemonize no#daemonize yes#g" /etc/redis/${PORT}.conf
sed -i "s#^pidfile /var/run/redis_[[:digit:]]*.pid#pidfile /var/run/redis_${PORT}.pid#g" /etc/redis/${PORT}.conf
sed -i "s#^port [[:digit:]]*#port ${PORT}#g" /etc/redis/${PORT}.conf
sed -i "s#^dir .*#dir /var/redis/${PORT}#g" /etc/redis/${PORT}.conf
sed -i "s#^bind .*#bind 127.0.0.1#g" /etc/redis/${PORT}.conf

# 5. 主从配置
PW="password"
if [ true ]; then
    # slave节点配置
    MASTER_HOST="127.0.0.1 6379"
    sed -i "s/^[# ]*replicaof .*/replicaof ${MASTER_HOST}/g" /etc/redis/${PORT}.conf
    sed -i "s/^replica-read-only .*/replica-read-only yes/g" /etc/redis/${PORT}.conf
    sed -i "s/^[# ]*masterauth .*/masterauth ${PW}/g" /etc/redis/${PORT}.conf
    sed -i "s/^[# ]*requirepass .*/requirepass ${PW}/g" /etc/redis/${PORT}.conf
else
    # master配置
    sed -i "s/^[# ]*requirepass .*/requirepass ${PW}/g" /etc/redis/${PORT}.conf
fi;

# 5. 启动脚本
/etc/init.d/redis_${PORT} start

# 6. 连接
redis-cli -h 127.0.0.1 -p ${PORT} -a ${PW}

# 7. 删除产生的文件
#redis-cli -h 127.0.0.1 -p ${PORT} -a ${PW} SHUTDOWN
redis-cli -h 127.0.0.1 -p ${PORT} SHUTDOWN
rm -rf /etc/init.d/redis_${PORT} /etc/redis/${PORT}.conf /var/redis/${PORT} /var/run/redis_${PORT}.pid
