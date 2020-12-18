#!/bin.bash
cd /opt
wget https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/zookeeper-3.5.5/apache-zookeeper-3.5.5-bin.tar.gz
tar zxvf apache-zookeeper-3.5.5-bin.tar.gz
mv apache-zookeeper-3.5.5-bin zookeeper
cd zookeeper

function prepare() {
    num=$1;
    if [ ! -d data/zk"${num}" ]; then
      mkdir -p data/zk"${num}"
      echo "${num}" > data/zk"${num}"/myid
    fi
    if [ ! -f conf/zoo"${num}".cfg ]; then
        cp conf/zoo_sample.cfg conf/zoo"${num}".cfg
        sed -i "s/clientPort=2181/clientPort=${num}2181/g" conf/zoo"${num}".cfg
        sed -i "s/dataDir=\/tmp\/zookeeper/\dataDir=\/opt\/zookeeper\/data\/zk${num}/g" conf/zoo"${num}".cfg
        {
            echo "server.1=127.0.0.1:12888:13888"
            echo "server.2=127.0.0.1:22888:23888"
            echo "server.3=127.0.0.1:32888:33888"
        } >> conf/zoo"${num}".cfg
    fi
    echo "prepare ${num} success!";
}

prepare 1
prepare 2
prepare 3
bin/zkServer.sh start conf/zoo1.cfg
bin/zkServer.sh start conf/zoo2.cfg
bin/zkServer.sh start conf/zoo3.cfg
