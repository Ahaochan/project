#!/bin/bash
# https://www.elastic.co/cn/downloads/beats/filebeat

# 1. 下载
cd /opt
wget https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-7.9.2-linux-x86_64.tar.gz
tar xvf filebeat-7.9.2-linux-x86_64.tar.gz
cd filebeat-7.9.2-linux-x86_64

# 2. 启动 es
vim filebeat.yml
./filebeat -e -c filebeat.yml
