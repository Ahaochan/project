#!/bin/bash
# 1. 下载prometheus
cd /opt
wget https://ghproxy.com/https://github.com/prometheus/prometheus/releases/download/v2.38.0/prometheus-2.38.0.linux-386.tar.gz
tar zxvf prometheus-2.38.0.linux-386.tar.gz
cd prometheus-2.37.1.linux-amd64/
./prometheus --version
# 2. 同步服务器时间
apt -y install ntpdate && ntpdate -u ntp.api.bz
# 3. 启动prometheus
#./prometheus --config.file=prometheus.yml
nohup ./prometheus --config.file=prometheus.yml &
curl 127.0.0.1:9090

# 4. 下载node-exporter
cd /opt
wget https://ghproxy.com/https://github.com/prometheus/node_exporter/releases/download/v1.3.1/node_exporter-1.3.1.linux-386.tar.gz
tar zxvf node_exporter-1.3.1.linux-386.tar.gz
cd node_exporter-1.3.1.linux-386/
./node_exporter --version
nohup ./node_exporter --web.listen-address=":9100" &

# 4. 下载grafana
cd /opt
wget https://dl.grafana.com/enterprise/release/grafana-enterprise-9.1.5.linux-amd64.tar.gz
tar zxvf grafana-enterprise-9.1.5.linux-amd64.tar.gz
cd grafana-9.1.5/
# 5. 启动grafana
nohup bin/grafana-server start &
curl http://127.0.0.1:3000/login
# 1 Node Exporter for Prometheus Dashboard CN v20200628：https://grafana.com/grafana/dashboards/12884-1-node-exporter-for-prometheus-dashboard-cn-v20200628/
# 1 SLS JVM监控大盘：https://grafana.com/grafana/dashboards/12856-jvm-micrometer/

