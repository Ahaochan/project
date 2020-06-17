#!/bin/bash
# 1. 环境变量设置
export IP=`hostname -I | awk '{print $1}'`
export NAMESRV_ADDR=${IP}:9876

# 2. 下载运行
docker run -d -e "JAVA_OPTS=-Drocketmq.namesrv.addr=${NAMESRV_ADDR} -Dcom.rocketmq.sendMessageWithVIPChannel=false" -p 8080:8080 -t styletang/rocketmq-console-ng
