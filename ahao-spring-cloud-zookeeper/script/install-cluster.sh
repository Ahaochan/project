#!/bin.bash
# 1. 安装 Zookeeper
cd /opt || return 1
wget https://archive.apache.org/dist/zookeeper/zookeeper-3.5.5/apache-zookeeper-3.5.5-bin.tar.gz
tar zxvf apache-zookeeper-3.5.5-bin.tar.gz
mv apache-zookeeper-3.5.5-bin zookeeper
cd zookeeper || return 1

# 2. 确保在 Zookeeper 目录下
if [ ! -f "conf/zoo_sample.cfg" ]; then
    echo '请进入zookeeper目录下'
    return 1
fi

# 3. 启动 Zookeeper 集群
COUNT=3;
for ((i=1;i<=COUNT;i++))
do
    # 3.1. 初始化 myid 文件
    if [ ! -d data/zk"${i}" ]; then
        mkdir -p data/zk"${i}"
    fi
    echo "${i}" > data/zk"${i}"/myid
    # 3.2. 初始化 cfg 配置文件
    if [ ! -f conf/zoo"${i}".cfg ]; then
        cp conf/zoo_sample.cfg conf/zoo"${i}".cfg
    fi
    sed -i "s@^clientPort=[[:digit:]]*@clientPort=${i}2181@g" conf/zoo"${i}".cfg
    sed -i "s@^dataDir=.*@dataDir=$(pwd)/data/zk${i}@g" conf/zoo"${i}".cfg
    sed -i "/^server.[[:digit:]]*=.*/d" conf/zoo"${i}".cfg
    # 3.3. 添加节点信息
    for ((j=1;j<=COUNT;j++))
    do
        echo "server.${j}=127.0.0.1:${j}2888:${j}3888" >> conf/zoo"${i}".cfg
    done
done
for ((i=1;i<=COUNT;i++))
do
    bin/zkServer.sh start conf/zoo${i}.cfg
done

# 4. 删除产生的文件
for ((i=1;i<=COUNT;i++))
do
    bin/zkServer.sh stop conf/zoo${i}.cfg
    rm -rf data/zk"${i}" conf/zoo"${i}".cfg
done
