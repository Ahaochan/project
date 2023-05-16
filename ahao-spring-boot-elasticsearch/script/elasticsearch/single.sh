#!/bin/bash
# https://www.elastic.co/cn/downloads/elasticsearch

# 1. 下载
cd /opt
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.8.1-linux-x86_64.tar.gz
tar xvf elasticsearch-7.8.1-linux-x86_64.tar.gz
cd elasticsearch-7.8.1

# 2. 系统参数调整
{
    echo "* soft nofile 65535"
    echo "* hard nofile 65535"
    echo "* soft nproc 4096"
    echo "* hard nproc 4096"
} >> /etc/security/limits.conf
echo "vm.max_map_count=262144" >> /etc/sysctl.conf && sysctl -p
export ES_JAVA_OPTS="-Xms256m -Xmx256m"

# 3. 创建es用户, es不能用root启动
groupadd es
useradd es -g es
chown -R es:es "$(pwd)"
mkdir -p data log
su - es

bin/elasticsearch -E node.name=node1 -E cluster.initial_master_nodes=node1 -E path.data=node1_data -E network.host=0.0.0.0 -E http.port=9200 -d

curl http://127.0.0.1:9200

# 3. 安装插件
bin/elasticsearch-plugin list
bin/elasticsearch-plugin install analysis-icu
