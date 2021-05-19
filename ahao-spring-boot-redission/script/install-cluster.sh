#!/bin/bash
# 1. 安裝依赖
apt-get install tcl make gcc -y

# 2. 安装 Redis
VERSION=6.2.2
wget https://download.redis.io/releases/redis-${VERSION}.tar.gz
tar xzf redis-${VERSION}.tar.gz
cd redis-${VERSION} || return 1
make distclean && make && make test && make install

# 3. 启动Redis
if [ ! -f "redis.conf" ]; then
    echo '请进入redis目录下'
    return 1
fi
PORTS=(7000 7001 7002 7003 7004 7005 7006 7007)
for PORT in ${PORTS[@]}
do
    # 3.1. 配置文件
    cp utils/redis_init_script /etc/init.d/redis_${PORT}
    sed -i "s#REDISPORT=[[:digit:]]*#REDISPORT=${PORT}#g" /etc/init.d/redis_${PORT}

    mkdir -p /etc/redis /var/redis/${PORT}
    cp redis.conf /etc/redis/${PORT}.conf
    sed -i "s#^daemonize no#daemonize yes#g" /etc/redis/${PORT}.conf
    sed -i "s#^pidfile /var/run/redis_[[:digit:]]*.pid#pidfile /var/run/redis_${PORT}.pid#g" /etc/redis/${PORT}.conf
    sed -i "s#^port [[:digit:]]*#port ${PORT}#g" /etc/redis/${PORT}.conf
    sed -i "s#^dir .*#dir /var/redis/${PORT}#g" /etc/redis/${PORT}.conf
    sed -i "s#^bind .*#bind 127.0.0.1#g" /etc/redis/${PORT}.conf

    # 3.2. cluster配置
    PW="password"
    #sed -i "s@^[# ]*requirepass .*@requirepass ${PW}/g" /etc/redis/${PORT}.conf
    sed -i "s@^[# ]*cluster-enabled .*@cluster-enabled yes@g" /etc/redis/${PORT}.conf
    sed -i "s@^[# ]*cluster-config-file .*@cluster-config-file /etc/redis/cluster-nodes-${PORT}.conf@g" /etc/redis/${PORT}.conf
    sed -i "s@^[# ]*cluster-node-timeout .*@cluster-node-timeout 5000@g" /etc/redis/${PORT}.conf

    # 3.3. 启动脚本
    /etc/init.d/redis_${PORT} start
done

# 4. 启动集群
redis-cli --cluster create --cluster-replicas 1 \
    127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005

# 6. 检查、连接
redis-cli --cluster check 127.0.0.1:7000
redis-cli -h 127.0.0.1 -p 7000 -c

# 7. 添加主节点
redis-cli --cluster add-node 127.0.0.1:7006 127.0.0.1:7000
redis-cli --cluster reshard 127.0.0.1:7000
# 4096 # 16384/4=4096
# 1f2ae2b0bb104f679ce7cb0c475e75943a84a122 # 新加的节点的id
# all # 从所有主节点迁移hash slot到新节点
# yes # 确定执行

# 8. 添加从节点
redis-cli --cluster add-node --cluster-slave --cluster-master-id 1f2ae2b0bb104f679ce7cb0c475e75943a84a122 \
    127.0.0.1:7007 127.0.0.1:7000

# 8. 迁移 hash slot 后删除节点
redis-cli --cluster reshard 127.0.0.1:7000 --cluster-from 1f2ae2b0bb104f679ce7cb0c475e75943a84a122 \
    --cluster-to cea8c3b57d21649ad4452a9262f686f0a1111f99 --cluster-slots 1365 --cluster-yes
redis-cli --cluster reshard 127.0.0.1:7000 --cluster-from 1f2ae2b0bb104f679ce7cb0c475e75943a84a122 \
    --cluster-to 8bfcd3f29cf7f2a09bda57e4a98c636c42bcf755 --cluster-slots 1365 --cluster-yes
redis-cli --cluster reshard 127.0.0.1:7000 --cluster-from 1f2ae2b0bb104f679ce7cb0c475e75943a84a122 \
    --cluster-to 4c3c71ebdc3b3514347680c177229ebe17295b63 --cluster-slots 1366 --cluster-yes
redis-cli --cluster del-node 127.0.0.1:7000 1f2ae2b0bb104f679ce7cb0c475e75943a84a122
redis-cli --cluster del-node 127.0.0.1:7000 e1bd8eaf0c6dc8a5132687daa19d1787623b8deb

# 7. 删除产生的文件
for PORT in ${PORTS[@]}
do
    # redis-cli -h 127.0.0.1 -p ${PORT} -a ${PW} SHUTDOWN
    redis-cli -h 127.0.0.1 -p ${PORT} SHUTDOWN
    rm -rf /etc/init.d/redis_${PORT} /etc/redis/${PORT}.conf /var/redis/${PORT} /var/run/redis_${PORT}.pid /etc/redis/cluster-nodes-${PORT}.conf
done
