#!/bin/bash
# 1. 下载 RocketMQ
cd /opt || return 1
wget https://archive.apache.org/dist/rocketmq/4.6.0/rocketmq-all-4.6.0-bin-release.zip
unzip rocketmq-all-4.6.0-bin-release.zip
cd /opt/rocketmq-all-4.6.0-bin-release/  || return 1

# 2. 确保在 RocketMQ 目录下
if [ ! -f "conf/broker.conf" ]; then
    echo '请进入RocketMQ目录下'
    return 1
fi
sed -i "s/Xms[[:digit:]]*g/Xms256m/g" bin/runserver.sh
sed -i "s/Xmx[[:digit:]]*g/Xmx256m/g" bin/runserver.sh
sed -i "s/Xmn[[:digit:]]*g/Xmn128m/g" bin/runserver.sh

sed -i "s/Xms[[:digit:]]*g/Xms128m/g" bin/runbroker.sh
sed -i "s/Xmx[[:digit:]]*g/Xmx256m/g" bin/runbroker.sh
sed -i "s/Xmn[[:digit:]]*g/Xmn256m/g" bin/runbroker.sh

# 3. 启动 NameServer
COUNT_NS=3;
ADDR_NS="";
for ((i=1;i<=COUNT_NS;i++))
do
    PORT="${i}9876";
    echo "listenPort=${PORT}" > conf/namesrv${i}.properties
    ADDR_NS="${ADDR_NS}127.0.0.1:$PORT;"
    nohup sh bin/mqnamesrv -c conf/namesrv${i}.properties &
done

# 4. 启动 Broker
COUNT_BROKER=3;
for ((i=1;i<=COUNT_BROKER;i++))
do
    mkdir -p data/broker${i}/{data,commitlog}

    echo "" > conf/broker${i}.properties
    {
      echo "brokerClusterName=ahao-cluster"   # 集群名称
      echo "brokerName=RaftNode00"            # 节点名称, 同一个 master 下的 slave 要保持一致
      echo "brokerIP1=$(hostname -I | awk '{print $1}')"
      echo "listenPort=309${i}1"              # Broker 端口号
      echo "namesrvAddr=${ADDR_NS}"           # NameServer 地址
      echo "storePathRootDir=$(pwd)/data/broker${i}/data"
      echo "storePathCommitLog=$(pwd)/data/broker${i}/commitlog"
      echo "enableDLegerCommitLog=true"       # 启动 DLeger 自动主备切换
      echo "dLegerGroup=RaftNode00"           # 和 brokerName 保持一致
      echo "dLegerPeers=n1-127.0.0.1:40911;n2-127.0.0.1:40912;n3-127.0.0.1:40913"
      echo "dLegerSelfId=n${i}"
      # 发送消息的线程数和CPU核数保持一致
      echo "sendMessageThreadPoolNums=$(grep -c ^processor /proc/cpuinfo)"
    } >> conf/broker${i}.properties

    nohup bin/mqbroker -c conf/broker${i}.properties &
done

# 5. 测试集群是否正常
sh bin/mqadmin clusterList -n 127.0.0.1:19876

# 7. 打完关机
sh bin/mqshutdown broker
sh bin/mqshutdown namesrv
for ((i=1;i<=COUNT_NS;i++))
do
    rm -rf conf/namesrv${i}.properties
done
for ((i=1;i<=COUNT_BROKER;i++))
do
    rm -rf data/broker${i}
done
