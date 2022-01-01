#!/bin/bash
# https://seata.io/zh-cn/docs/ops/deploy-by-docker.html
IP=$(hostname -I | awk '{print $1}')
docker run --name seata-server \
        -p 8091:8091 \
        -e SEATA_IP="${IP}" \
        -e SEATA_PORT=8091 \
        -e STORE_MODE=file \
        seataio/seata-server:1.3.0
