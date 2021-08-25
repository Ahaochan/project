#!/bin/bash
# 1. 安裝依赖
docker run -p 2181:2181 -p 2888:2888 -p 3888:3888 -p 8080:8080 zookeeper

VERSION=1.1.0
wget https://archive.apache.org/dist/storm/apache-storm-${VERSION}/apache-storm-${VERSION}.tar.gz
tar xzf apache-storm-${VERSION}.tar.gz
mv apache-storm-${VERSION} storm
cd storm || return 1

vim conf/storm.yaml
# 配置zk地址
# storm.zookeeper.servers:
#     - "127.0.0.1"
# 配置nimbus监控节点
# nimbus.seeds: ["127.0.0.1"]
# 追加
# storm.local.dir: "/opt/storm/data"
# 每个机器启动2个worker，端口号分别是6700和6701
# supervisor.slots.ports:
#     - 6700
#     - 6701
# ui.port: 8081


# nohup bin/storm nimbus >> /dev/null &
bin/storm nimbus
bin/storm supervisor
bin/storm ui


