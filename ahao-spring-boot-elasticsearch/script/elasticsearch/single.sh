#!/bin/bash
# https://www.elastic.co/cn/downloads/elasticsearch

# 1. 下载
cd /opt
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.8.1-linux-x86_64.tar.gz
tar xvf elasticsearch-7.8.1-linux-x86_64.tar.gz
cd elasticsearch-7.8.1

sudo sysctl -w vm.max_map_count=262144
sed -i "s/#network.host: 192.168.0.1/network.host: 0.0.0.0/" config/elasticsearch.yml

# 2. 启动 es
bin/elasticsearch -E node.name=node1 -E network.host=0.0.0.0
curl http://127.0.0.1:9200

# 3. 安装插件
bin/elasticsearch-plugin list
bin/elasticsearch-plugin install analysis-icu
