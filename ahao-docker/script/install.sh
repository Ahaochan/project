#!/bin/bash
# 修改 yum docker 镜像地址
curl https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo -o /etc/yum.repos.d/docker.repo
# 缓存安装信息
yum makecache fast
# 安装 docker
yum install -y docker-ce

# 启动 docker 并设置为开机启动
systemctl start docker && systemctl enable docker

# 添加国内镜像
mkdir -p /etc/docker
tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://registry.docker-cn.com"],
  "insecure-registries": ["127.0.0.1"]
}
EOF
systemctl daemon-reload
systemctl restart docker

# 安装 docker compose
curl -SL https://ghproxy.com/https://github.com/docker/compose/releases/download/v2.11.2/docker-compose-linux-x86_64 -o /usr/bin/docker-compose
chmod +x /usr/bin/docker-compose
docker-compose --version
