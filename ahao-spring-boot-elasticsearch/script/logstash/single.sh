#!/bin/bash
# https://www.elastic.co/cn/downloads/logstash

# 1. 下载
cd /opt || exit
wget https://artifacts.elastic.co/downloads/logstash/logstash-7.8.1.tar.gz
tar xvf logstash-7.8.1.tar.gz

# 2. 启动 logstash
bin/logstash -f logstash.conf
