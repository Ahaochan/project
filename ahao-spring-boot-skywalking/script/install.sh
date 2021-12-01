#!/bin/bash
cd /opt
# http://skywalking.apache.org/downloads/
wget https://archive.apache.org/dist/skywalking/8.7.0/apache-skywalking-apm-es7-8.7.0.tar.gz
tar -zxvf apache-skywalking-apm-es7-8.7.0.tar.gz
cd apache-skywalking-apm-bin-es7

# 启动SkyWalking后端服务
bin/oapService.sh
# 启动SkyWalking前端服务
bin/webappService.sh

curl 127.0.0.1:8080
