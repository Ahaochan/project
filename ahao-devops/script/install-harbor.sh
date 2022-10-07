#!/bin/bash
cd /opt
wget https://ghproxy.com/https://github.com/goharbor/harbor/releases/download/v1.10.14/harbor-online-installer-v1.10.14.tgz
tar xvzf harbor-online-installer-v1.10.14.tgz
cd harbor

sed -i "s/hostname:.*/hostname: harbor.ahao.moe/g" harbor.yml
sed -i "s/port: 80/port: 3080/g" harbor.yml
sed -i "s/port: 443/port: 3443/g" harbor.yml
# 临时禁用https
sed -i "s/^https:/#https:/g" harbor.yml

# 生成 docker-compose.yml, 并启动 docker 容器
./install.sh

# 登录 admin / Harbor12345
curl 127.0.0.1:3443
