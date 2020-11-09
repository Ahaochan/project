#!/bin/bash
# https://kafka.apache.org/downloads

# 1. 配置 JVM 参数
KAFKA_HEAP_OPTS=" -Xms6g  -Xmx6g"
KAFKA_JVM_PERFORMANCE_OPTS=" -server -XX:+UseG1GC -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=35 -XX:+ExplicitGCInvokesConcurrent -Djava.awt.headless=true"
IP=`hostname -I | awk '{print $1}'`
export KAFKA_HEAP_OPTS KAFKA_JVM_PERFORMANCE_OPTS IP

# 2. 安装启动
wget https://mirror.bit.edu.cn/apache/kafka/2.6.0/kafka_2.13-2.6.0.tgz
tar zxvf kafka_2.13-2.6.0.tgz
cd kafka_2.13-2.6.0
bin/kafka-server-start.sh config/server.properties \
    --override broker.id=0 \
    --override log.dirs=/tmp/kafka-logs \
    --override listeners=PLAINTEXT://"${IP}":9092 \
    --override advertised.listeners=PLAINTEXT://"${IP}":9092

# 3. topic 操作
sh bin/kafka-topics.sh --create --topic ahao-topic --replication-factor 1 --partitions 1 --zookeeper 127.0.0.1:2181 --config retention.ms=15552000000 --config max.message.bytes=5242880
sh bin/kafka-topics.sh --list --zookeeper 127.0.0.1:2181
sh bin/kafka-run-class.sh kafka.admin.TopicCommand --delete --topic ahao-topic --zookeeper 127.0.0.1:2181

# 4. 生产消费消息
sh bin/kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic ahao-topic
sh bin/kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic ahao-topic --from-beginning
