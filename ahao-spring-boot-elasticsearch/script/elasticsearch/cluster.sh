#!/bin/bash
# https://www.elastic.co/cn/downloads/elasticsearch

# 1. 下载
cd /opt || exit
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.8.1-linux-x86_64.tar.gz
tar xvf elasticsearch-7.8.1-linux-x86_64.tar.gz

# 2. 启动 es
bin/elasticsearch -E node.name=node1 -E cluster.name=ahao-cluster -E path.data=node1_data -d
bin/elasticsearch -E node.name=node2 -E cluster.name=ahao-cluster -E path.data=node2_data -d
bin/elasticsearch -E node.name=node3 -E cluster.name=ahao-cluster -E path.data=node3_data -d
curl http://127.0.0.1:9200/_cat/nodes

# 3. 结束进程
ps grep | elasticsearch / kill pid
