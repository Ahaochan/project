#!/bin/bash
LOG_PATH="/var/log/nginx"
RECORD_TIME=$(date -d "yesterday" +%Y-%m-%d)
PID=/var/run/nginx/nginx.pid
mv ${LOG_PATH}/access.log ${LOG_PATH}/access."${RECORD_TIME}".log
mv ${LOG_PATH}/error.log ${LOG_PATH}/error."${RECORD_TIME}".log

# 向 nginx 主进程发送信号, 用于重新打开日志文件
kill -USR1 `cat $PID`

# chmod +x log_cut.sh
# ./log_cut.sh
# */1 * * * * /opt/log_cut.sh
