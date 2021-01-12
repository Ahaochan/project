#!/bin/bash
# 1. 下载
# http://nginx.org/en/download.html
cd /opt
wget http://nginx.org/download/nginx-1.18.0.tar.gz
tar zxvf nginx-1.18.0.tar.gz
cd nginx-1.18.0

# 2. 编译安装
# https://www.nginx.com/resources/wiki/start/topics/tutorials/gettingstarted/
apt-get install build-essential libtool libpcre3 libpcre3-dev zlib1g-dev openssl -y
./configure
make
sudo make install

# 3. 启动nginx
/usr/local/nginx/sbin/nginx
curl 127.0.0.1:80

# 4. 关闭nginx
./nginx -s stop # 强制关闭
./nginx -s quit # 等连接处理完毕再关闭
./nginx -t # 测试配置文件是否正确
