#!/bin/bash
# https://www.elastic.co/cn/downloads/elasticsearch

# 1. 下载
cd /opt || exit
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.8.1-linux-x86_64.tar.gz
tar xvf elasticsearch-7.8.1-linux-x86_64.tar.gz

# 2. 启动 es
export ES_JAVA_OPTS="-Xms256m -Xmx256m"
bin/elasticsearch -E node.name=node1 -E cluster.initial_master_nodes=node1 -E cluster.name=ahao-cluster -E path.data=node1_data -E network.host=0.0.0.0 -E http.port=19200 -d
bin/elasticsearch -E node.name=node2 -E cluster.initial_master_nodes=node1 -E cluster.name=ahao-cluster -E path.data=node2_data -E network.host=0.0.0.0 -E http.port=29200 -d
bin/elasticsearch -E node.name=node3 -E cluster.initial_master_nodes=node1 -E cluster.name=ahao-cluster -E path.data=node3_data -E network.host=0.0.0.0 -E http.port=39200 -d
curl http://127.0.0.1:9200/_cat/nodes

# 3. 结束进程
ps aux | grep elasticsearch
