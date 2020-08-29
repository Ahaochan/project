#!/bin/bash
# https://www.elastic.co/cn/downloads/kibana

# 1. 下载
cd /opt || exit
wget https://artifacts.elastic.co/downloads/kibana/kibana-7.8.1-linux-x86_64.tar.gz
tar xvf kibana-7.8.1-linux-x86_64.tar.gz

# 设置elasticsearch.url
vim config/kibana.yml

# 2. 启动 kibana
bin/kibana
curl http://127.0.0.1:5601

# 3. 安装插件
bin/kibana-plugin list
bin/kibana-plugin install plugin_location
bin/kibana remove
