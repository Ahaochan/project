#!/bin/bash
VERSION=1.3.0
wget https://ghproxy.com/https://github.com/seata/seata/releases/download/v${VERSION}/seata-server-${VERSION}.tar.gz
tar zxvf seata-server-${VERSION}.tar.gz
cd seata/ || exit

# 修改JVM参数
sed -i "s/Xmx[[:digit:]]*m/Xmx512m/g" bin/seata-server.sh
sed -i "s/Xms[[:digit:]]*m/Xms512m/g" bin/seata-server.sh
sed -i "s/Xmn[[:digit:]]*m/Xmn256m/g" bin/seata-server.sh

# 启动
mkdir logs && touch logs/seata_gc.log
bin/seata-server.sh

# 数据库添加undo_log表
wget "https://ghproxy.com/https://raw.githubusercontent.com/seata/seata/develop/script/client/at/db/mysql.sql" -O /tmp/seata-at.sql
mysql -h 127.0.0.1 -P 3306 -u root -proot -D ahaodb < /tmp/seata-at.sql
