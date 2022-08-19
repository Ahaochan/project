#!/bin/bash

mkdir -p /opt/rabbitmq/config && touch /opt/rabbitmq/config/rabbitmq.config
docker run -it -d --name rabbitmq -p 5672:5672 -p 15672:15672 \
    --restart always \
    -v /opt/rabbitmq/config/rabbitmq.config:/etc/rabbitmq/rabbitmq.config \
    --hostname my-rabbit \
    rabbitmq:3-management
