#!/bin/bash
# 1. 安装 mysql
#    https://gist.github.com/Ahaochan/707cec4f1daed3190dd8a256b37406ec#file-mysql-sh-L1-L23

# 2. 安装 apollo
cd /opt/
git clone https://github.com/nobodyiam/apollo-build-scripts.git
cd apollo-build-scripts/
mysql -h 127.0.0.1 -P 3306 -u root -proot < sql/apolloportaldb.sql
mysql -h 127.0.0.1 -P 3306 -u root -proot < sql/apolloconfigdb.sql

# 3. 编辑 demo.sh 的数据库连接信息
vim demo.sh
