#!/bin/bash
# 1. 安裝依赖
apt-get install tcl make gcc -y

# 2. 安装 Redis
VERSION=6.2.2
wget https://download.redis.io/releases/redis-${VERSION}.tar.gz
tar xzf redis-${VERSION}.tar.gz
cd redis-${VERSION} || return 1
make distclean && make && make test && make install

# 3. 确保在 Redis 目录下
if [ ! -f "redis.conf" ]; then
    echo '请进入redis目录下'
    return 1
fi

# 3. 开机时启动
PORTS=(26379 26380 26381)
PW="password"

# 4. 配置文件
for ((i=0;i<${#PORTS[@]};i++))
do
    PORT=${PORTS[i]}
    mkdir -p /etc/sentinel /var/sentinel/"${PORT}"
    mkdir -p /var/log/sentinel && touch /var/log/sentinel/"${PORT}"
    cp sentinel.conf /etc/sentinel/"${PORT}".conf
    sed -i "s#^sentinel monitor mymaster .*#sentinel monitor mymaster 127.0.0.1 6379 2#g" /etc/sentinel/"${PORT}".conf
    sed -i "s#^sentinel down-after-milliseconds mymaster [[:digit:]]*#sentinel down-after-milliseconds mymaster 30000#g" /etc/sentinel/"${PORT}".conf
    sed -i "s#^sentinel parallel-syncs mymaster [[:digit:]]*#sentinel parallel-syncs mymaster 1#g" /etc/sentinel/"${PORT}".conf
    sed -i "s#^sentinel failover-timeout mymaster [[:digit:]]*#sentinel failover-timeout mymaster 180000#g" /etc/sentinel/"${PORT}".conf
    sed -i "s#^[# ]*bind .*#bind 127.0.0.1#g" /etc/sentinel/"${PORT}".conf
    sed -i "s#^port [[:digit:]]*#port ${PORT}#g" /etc/sentinel/"${PORT}".conf
    sed -i "s#^daemonize no#daemonize yes#g" /etc/sentinel/"${PORT}".conf
    sed -i "s#^pidfile /var/run/redis-sentinel[[:digit:]]*.pid#pidfile /var/run/redis-sentinel${PORT}.pid#g" /etc/sentinel/"${PORT}".conf
    sed -i "s#^logfile .*#logfile /var/log/sentinel/${PORT}#g" /etc/sentinel/"${PORT}".conf
    sed -i "s#^dir .*#dir /var/sentinel/${PORT}#g" /etc/sentinel/"${PORT}".conf
    echo "sentinel auth-pass mymaster ${PW}" >> /etc/sentinel/"${PORT}".conf

    # 5. 启动脚本
    redis-sentinel /etc/sentinel/"${PORT}".conf
done

# 6. 连接
redis-cli -h 127.0.0.1 -p 26379
sentinel master mymaster
sentinel slaves mymaster
sentinel sentinels mymaster
sentinel get-master-addr-by-name mymaster

# 7. 删除产生的文件
for ((i=0;i<${#PORTS[@]};i++))
do
    PORT=${PORTS[i]}
    redis-cli -h 127.0.0.1 -p "${PORT}" SHUTDOWN
    rm -rf /var/sentinel/"${PORT}".conf /var/sentinel/"${PORT}" /var/run/redis-sentinel"${PORT}".pid /var/log/sentinel/"${PORT}"
done
