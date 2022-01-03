#!/bin/bash
VERSION=1.3.0
wget https://ghproxy.com/https://github.com/seata/seata/releases/download/v${VERSION}/seata-server-${VERSION}.tar.gz
tar zxvf seata-server-${VERSION}.tar.gz
cd seata/ || exit

# 修改JVM参数
vim bin/seata-server.sh

# 启动
mkdir logs && touch logs/seata_gc.log
bin/seata-server.sh

# 数据库添加undo_log表
# https://ghproxy.com/https://github.com/seata/seata/blob/1.3.0/script/client/at/db/mysql.sql
