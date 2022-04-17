#!/bin.bash
cd /opt
wget https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/zookeeper-3.5.5/apache-zookeeper-3.5.5-bin.tar.gz
tar zxvf apache-zookeeper-3.5.5-bin.tar.gz
mv apache-zookeeper-3.5.5-bin zookeeper
cd zookeeper
cp conf/zoo_sample.cfg conf/zoo.cfg

bin/zkServer.sh start
bin/zkServer.sh restart
bin/zkServer.sh stop

curl http://127.0.0.1:8080/commands/stats
# 访问`http://虚拟机IP:8080/commands/stats`, 成功访问
