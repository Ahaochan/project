#!/bin/bash
# 1. 环境变量设置
if [ -z ${JAVA_HOME} ]; then export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64; fi;
echo 'JAVA_HOME: '$JAVA_HOME

export IP=`hostname -I | awk '{print $1}'`
export NAMESRV_ADDR=${IP}:9876

# 2. 下载 RocketMQ
cd /opt
wget https://archive.apache.org/dist/rocketmq/4.6.0/rocketmq-all-4.6.0-bin-release.zip
unzip rocketmq-all-4.6.0-bin-release.zip
cd rocketmq-all-4.6.0-bin-release/

# 3. JVM 参数调整
sed -i "s/Xms[[:digit:]]*g/Xms1g/g" bin/runserver.sh
sed -i "s/Xmx[[:digit:]]*g/Xmx1g/g" bin/runserver.sh
sed -i "s/Xmn[[:digit:]]*g/Xmn1g/g" bin/runserver.sh

sed -i "s/Xms[[:digit:]]*g/Xms1g/g" bin/runbroker.sh
sed -i "s/Xmx[[:digit:]]*g/Xmx1g/g" bin/runbroker.sh
sed -i "s/Xmn[[:digit:]]*g/Xmn1g/g" bin/runbroker.sh

# 4. 启动 name server
mkdir logs
nohup sh bin/mqnamesrv -n ${NAMESRV_ADDR} > logs/namesrv.log 2>&1 &
tail -fn 300 logs/namesrv.log

# 5. 启动 broker
nohup sh bin/mqbroker -n ${NAMESRV_ADDR} -c conf/broker.conf autoCreateTopicEnable=true > logs/mqbroker.log 2>&1 &
tail -fn 300 logs/mqbroker.log

# 6. 测试MQ正常收发消息
sh bin/tools.sh org.apache.rocketmq.example.quickstart.Producer
sh bin/tools.sh org.apache.rocketmq.example.quickstart.Consumer

# 7. 打完关机
sh bin/mqshutdown broker
sh bin/mqshutdown namesrv
