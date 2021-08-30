#!/bin/bash
# https://kafka.apache.org/downloads

# 1. 安装 Kafka
cd /opt || return 1
wget https://archive.apache.org/dist/kafka/2.6.0/kafka_2.13-2.6.0.tgz
tar zxvf kafka_2.13-2.6.0.tgz
cd kafka_2.13-2.6.0 || return 1

# 2. 确保在 Kafka 目录下
if [ ! -f "bin/kafka-server-start.sh" ]; then
    echo '请进入Kafka目录下'
    return 1
fi

# 3. 配置参数
KAFKA_HEAP_OPTS=" -Xms2g  -Xmx2g"
KAFKA_JVM_PERFORMANCE_OPTS=" -server -XX:+UseG1GC -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=35 -XX:+ExplicitGCInvokesConcurrent -Djava.awt.headless=true"
IP=$(hostname -I | awk '{print $1}')
ZK="127.0.0.1:12181,127.0.0.1:22181,127.0.0.1:32181/kafka-chroot1"
export KAFKA_HEAP_OPTS KAFKA_JVM_PERFORMANCE_OPTS

# 4. 修改配置文件
i=1
LOG_DIR="$(pwd)"/logs/kafka"${i}";
mkdir -p "${LOG_DIR}"

cp config/server.properties config/server"${i}".properties
CONFIG="config/server${i}.properties"
sed -i "/^broker.id=[[:digit:]]*/d" ${CONFIG} && echo "broker.id=${i}" >> ${CONFIG}
sed -i "s@^[#]*listeners=.*@listeners=PLAINTEXT://${IP}:${i}9092@g" ${CONFIG}
sed -i "s@^[#]*advertised.listeners=.*@advertised.listeners=PLAINTEXT://${IP}:${i}9092@g" ${CONFIG}
# ZooKeeper配置
sed -i "s@^[#]*zookeeper.connect=.*@zookeeper.connect=${ZK}@g" ${CONFIG}
# 以逗号分隔的目录列表, 用于存储日志文件
sed -i "s@^[#]*log.dirs=.*@log.dirs=${LOG_DIR}@g" ${CONFIG}
# 日志文件最多保留 7*24=168 小时
sed -i "s@^[#]*log.retention.hours=.*@log.retention.hours=168@g" ${CONFIG}
# Broker 能够接收的最大消息大小
sed -i "/^message.max.bytes=[[:digit:]]*/d" ${CONFIG} && echo "message.max.bytes=10000120" >> ${CONFIG}
# 不允许自动创建 topic
sed -i "/^auto.create.topics.enable=.*/d" ${CONFIG} && echo "auto.create.topics.enable=false" >> ${CONFIG}
# 不允许数据同步进度落后的副本参与竞选 leader, 避免造成消息丢失, 落后进度取决于broker端参数replica.lag.time.max.ms
sed -i "/^unclean.leader.election.enable=.*/d" ${CONFIG} && echo "unclean.leader.election.enable=false" >> ${CONFIG}
# 不允许定期进行 leader 选举
sed -i "/^auto.leader.rebalance.enable=.*/d" ${CONFIG} && echo "auto.leader.rebalance.enable=false" >> ${CONFIG}
bin/kafka-server-start.sh -daemon ${CONFIG}

# 5. topic 操作
bin/kafka-topics.sh --create --topic ahao-topic --replication-factor 1 --partitions 1 --zookeeper ${ZK} --config retention.ms=15552000000 --config max.message.bytes=5242880
bin/kafka-topics.sh --list --zookeeper ${ZK}
bin/kafka-run-class.sh kafka.admin.TopicCommand --delete --topic ahao-topic --zookeeper ${ZK}

# 6. 生产消费消息
bin/kafka-console-producer.sh --broker-list "${IP}:${i}"9092 --topic ahao-topic
bin/kafka-console-consumer.sh --bootstrap-server "${IP}:${i}"9092 --topic ahao-topic --from-beginning
