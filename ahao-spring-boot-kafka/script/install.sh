#!/bin/bash
# https://kafka.apache.org/downloads

# 1. 安装启动
wget https://mirror.bit.edu.cn/apache/kafka/2.6.0/kafka_2.13-2.6.0.tgz
tar zxvf kafka_2.13-2.6.0.tgz
cd kafka_2.13-2.6.0
sh bin/kafka-server-start.sh config/server.properties

# 2. topic 操作
sh bin/kafka-topics.sh --create --topic ahao-topic --replication-factor 1 --partitions 1 --zookeeper 127.0.0.1:2181
sh bin/kafka-topics.sh --list --zookeeper 127.0.0.1:2181
sh bin/kafka-run-class.sh kafka.admin.TopicCommand --delete --topic ahao-topic --zookeeper 127.0.0.1:2181

# 3. 生产消费消息
sh bin/kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic ahao-topic
sh bin/kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic ahao-topic --from-beginning
