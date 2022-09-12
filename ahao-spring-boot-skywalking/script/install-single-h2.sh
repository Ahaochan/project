#!/bin/bash
cd /opt
# 1. 下载安装
# http://skywalking.apache.org/downloads/
wget https://archive.apache.org/dist/skywalking/8.7.0/apache-skywalking-apm-es7-8.7.0.tar.gz
tar -zxvf apache-skywalking-apm-es7-8.7.0.tar.gz
cd apache-skywalking-apm-bin-es7

# 2. 修改配置
export SW_CLUSTER=standalone
export SW_STORAGE=h2
export JAVA_OPTS=" -Xms256M -Xmx512M "
# 启动SkyWalking后端服务
bin/oapService.sh
tail -fn 300 logs/skywalking-oap-server.log
# 启动SkyWalking前端服务
bin/webappService.sh
tail -fn 300 logs/webapp-console.log


curl 127.0.0.1:8080
