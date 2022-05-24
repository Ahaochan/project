#!/bin/bash
# 1. 下载安装
VERSION=2.1.0
wget https://github.com/alibaba/nacos/releases/download/${VERSION}/nacos-server-${VERSION}.tar.gz -O nacos-server.tar.gz
tar zxvf nacos-server.tar.gz
cd nacos || exit

# 2. 启动
bash bin/startup.sh -m standalone

# 3. 服务发现
curl -X GET 'http://127.0.0.1:8848/nacos/v1/ns/instance/list?serviceName=nacos.naming.serviceName'

# 4. 获取配置
curl -X GET "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=nacos.cfg.dataId&group=test"

# 5. 关机
bin/shutdown.sh
