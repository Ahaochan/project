#!/bin/bash
# https://www.elastic.co/cn/downloads/elasticsearch

# 1. 下载
cd /opt
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.8.1-linux-x86_64.tar.gz
tar xvf elasticsearch-7.8.1-linux-x86_64.tar.gz
cd elasticsearch-7.8.1

# 2. 启动 es
export ES_JAVA_OPTS="-Xms256m -Xmx256m"
sudo sysctl -w vm.max_map_count=262144
bin/elasticsearch -E node.name=node1 -E cluster.initial_master_nodes=node1 -E network.host=0.0.0.0 -E http.port=9200
curl http://127.0.0.1:9200

# 3. 安装插件
bin/elasticsearch-plugin list
bin/elasticsearch-plugin install analysis-icu
