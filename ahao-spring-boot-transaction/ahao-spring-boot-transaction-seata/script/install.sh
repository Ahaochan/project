#!/bin/bash
VERSION=1.3.0
wget https://ghproxy.com/https://github.com/seata/seata/releases/download/v${VERSION}/seata-server-${VERSION}.tar.gz
tar zxvf seata-server-${VERSION}.tar.gz
cd seata/ || exit

# 修改JVM参数
sed -i "s/Xmx[[:digit:]]*m/Xmx512m/g" bin/seata-server.sh
sed -i "s/Xms[[:digit:]]*m/Xms512m/g" bin/seata-server.sh
sed -i "s/Xmn[[:digit:]]*m/Xmn256m/g" bin/seata-server.sh

# 修改conf/file.conf的store配置参数
DB_URL="jdbc:mysql:\/\/127.0.0.1:3306\/seata?useUnicode=true\&rewriteBatchedStatements=true"
DB_USER="root"
DB_PW="root"
DB_MAX_CONN=1000;
DB_QUERY_LIMIT=500;
sed -i "s/mode = \".*\"/mode = \"db\"/g" conf/file.conf
sed -i "s/datasource = \".*\"/datasource = \"druid\"/g" conf/file.conf
sed -i "s/url = \".*\"/url = \"${DB_URL}\"/g" conf/file.conf
sed -i "s/user = \".*\"/user = \"${DB_USER}\"/g" conf/file.conf
sed -i "s/password = \".*\"/password = \"${DB_PW}\"/g" conf/file.conf
sed -i "s/maxConn = [[:digit:]]*/maxConn = ${DB_MAX_CONN}/g" conf/file.conf
sed -i "s/queryLimit = [[:digit:]]*/queryLimit = ${DB_QUERY_LIMIT}/g" conf/file.conf

wget "https://github.com/seata/seata/develop/script/server/db/mysql.sql" -O /tmp/seata-server.sql
mysql -h 127.0.0.1 -P 3306 -u root -proot -D seata < /tmp/seata-server.sql

# 修改conf/registry.conf的nacos配置
NACOS_URL="127.0.0.1:8848"
NACOS_USER="nacos"
NACOS_PW="nacos"
sed -i "s/type = \".*\"/type = \"nacos\"/g" conf/registry.conf
sed -i "s/serverAddr = \".*\"/serverAddr = \"${NACOS_URL}\"/g" conf/registry.conf
sed -i "s/serverAddr = \".*\"/serverAddr = \"${NACOS_URL}\"/g" conf/registry.conf
sed -i "s/username = \".*\"/username = \"${NACOS_USER}\"/g" conf/registry.conf
sed -i "s/password = \".*\"/password = \"${NACOS_PW}\"/g" conf/registry.conf

# 加载mysql驱动
mv lib/jdbc/mysql-connector-java-8.0.19.jar lib/

# 上传seata配置到nacos
wget https://github.com/seata/seata/raw/develop/script/config-center/config.txt
sed -i "s/store.mode=.*/store.mode=db/g" config.txt
sed -i "s/store.db.datasource=.*/store.db.datasource=druid/g" config.txt
sed -i "s/store.db.url=.*/store.db.url=${DB_URL}/g" config.txt
sed -i "s/store.db.user=.*/store.db.user=${DB_USER}/g" config.txt
sed -i "s/store.db.password=.*/store.db.password=${DB_PW}/g" config.txt
sed -i "s/store.db.maxConn=.*/store.db.maxConn=${DB_MAX_CONN}/g" config.txt
sed -i "s/store.db.queryLimit=.*/store.db.queryLimit=${DB_QUERY_LIMIT}/g" config.txt
wget https://github.com/seata/seata/raw/develop/script/config-center/nacos/nacos-config.sh -O bin/nacos-config.sh
wget https://github.com/seata/seata/raw/develop/script/config-center/nacos/nacos-config.py -O bin/nacos-config.py
cd bin
bash ./nacos-config.sh -h 127.0.0.1 -p 8848 -g SEATA_GROUP -u ${NACOS_USER} -w ${NACOS_PW}
cd -

# 启动seata
IP=`hostname -I | awk '{print $1}'`
mkdir logs && touch logs/seata_gc.log
bin/seata-server.sh -h "${IP}"

# 数据库添加undo_log表
# wget "https://ghproxy.com/https://raw.githubusercontent.com/seata/seata/develop/script/client/at/db/mysql.sql" -O /tmp/seata-at.sql
# mysql -h 127.0.0.1 -P 3306 -u root -proot -D ahaodb < /tmp/seata-at.sql
