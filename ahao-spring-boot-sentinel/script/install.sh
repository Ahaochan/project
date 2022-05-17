#!/bin/bash

# 1. 下载
VERSION=1.8.1
wget https://github.com/alibaba/Sentinel/releases/download/${VERSION}/sentinel-dashboard-${VERSION}.jar -O sentinel-dashboard.jar

# 2. 启动
java -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar

# 默认用户名和密码都是 sentinel
