#!/bin/bash

cd /opt
# 1. 开启binlog和创建canal用户
DB_IP="127.0.0.1"
DB_PORT="3306"
DB_USER="root"
DB_PW="root"
mysql -h ${DB_IP} -P ${DB_PORT} -u ${DB_USER} -p${DB_PW} -e "show variables like '%log_bin%'"

# 开启binlog
vim /etc/mysql/my.cnf
# [mysqld]
# log-bin=mysql-bin # 开启binlog
# binlog-format=ROW # 选择ROW模式
# server_id=1 # 配置MySQL replication需要定义, 不要和canal的slaveId重复

# 重启Mysql
service mysqld restart

mysql -h ${DB_IP} -P ${DB_PORT} -u ${DB_USER} -p${DB_PW} -e "
    CREATE USER canal IDENTIFIED BY 'canal';
    GRANT SELECT,REPLICATION SLAVE,REPLICATION CLIENT ON *.* TO 'canal'@'%';
    FLUSH PRIVILEGES;"
mysql -h ${DB_IP} -P ${DB_PORT} -u 'canal' -p'canal' -e "select now();"

# 2. 安装canal-admin
mkdir canal-admin && cd canal-admin
wget https://ghproxy.com/https://github.com/alibaba/canal/releases/download/canal-1.1.5/canal.admin-1.1.5.tar.gz
tar zxvf canal.admin-1.1.5.tar.gz
# 2.1. 初始化数据库SQL
mysql -h ${DB_IP} -P ${DB_PORT} -u ${DB_USER} -p${DB_PW} < conf/canal_manager.sql
# 2.2. 修改数据库账号密码, 需要有写权限
sed -i 's@username:.*@username: root@g' conf/application.yml
sed -i 's@password:.*@password: root@g' conf/application.yml
# 2.2. 修改配置信息, 密码默认是123456
sed -i 's@adminPasswd:.*@adminPasswd: 123456@g' conf/application.yml
sed -i 's@JAVA_OPTS="-server.*"@JAVA_OPTS="-server -Xms512m -Xmx512m"' conf/startup.sh
# 2.3. 启动canal-admin
bin/startup.sh
bin/stop.sh
# kill 9 "$(jps | grep Canal | awk '{print $1}')" && rm -rf bin/admin.pid
curl 127.0.0.1:8089
cd -

# 3. 安装canal-server
IP=$(hostname -I | awk '{print $1}')
mkdir canal-server && cd canal-server
wget https://ghproxy.com/https://github.com/alibaba/canal/releases/download/canal-1.1.5/canal.deployer-1.1.5.tar.gz
tar zxvf canal.deployer-1.1.5.tar.gz
# 设置canal-server本机ip
sed -i "s@canal.register.ip =.*@canal.register.ip = ${IP}@g" conf/canal_local.properties
# 设置canal-admin的地址和密码123456的密文, select substr(password('123456'), 2)
sed -i "s@canal.admin.manager =.*@canal.admin.manager = ${IP}:8089@g" conf/canal_local.properties
sed -i "s@canal.admin.passwd =.*@canal.admin.passwd = 6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9@g" conf/canal_local.properties
# 开启自动注册到canal-admin上
sed -i "s@canal.admin.register.auto =.*@canal.admin.register.auto = true@g" conf/canal_local.properties
# 设置canal-server注册到canal-admin上的名称
sed -i "s@canal.admin.register.name =.*@canal.admin.register.name = ahao-canal@g" conf/canal_local.properties
sed -i "s@Xms[[:digit:]]*m@Xms512m@g" bin/startup.sh
sed -i "s@Xmx[[:digit:]]*m@Xmx512m@g" bin/startup.sh
sed -i "s@Xmn[[:digit:]]*m@Xmn256m@g" bin/startup.sh
echo "" >> conf/canal_local.properties
echo "canal.serverMode=rocketMQ" >> conf/canal_local.properties
echo "canal.mq.servers=${IP}:9876" >> conf/canal_local.properties
# 启动canal-server, 这里因为使用了canal-admin进行管理, 所以要指定local
bin/stop.sh && rm -rf logs/canal/* && bin/startup.sh local
cat logs/canal/canal.log
