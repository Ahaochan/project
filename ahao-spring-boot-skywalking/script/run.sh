#!/bin/bash
# IDEA就复制到VM options里
java -jar -javaagent:/opt/skywalking-agent.jar \
     -Dskywalking.agent.service_name=ahao-spring-boot-skywalking \
     -Dskywalking.collector.backend_service=192.168.19.128:11800 \
     -Dskywalking.plugin.toolkit.log.grpc.reporter.server_host=192.168.19.128 \
     -Dskywalking.plugin.toolkit.log.grpc.reporter.server_port=11800 \
     -Dskywalking.plugin.toolkit.log.grpc.reporter.max_message_size=10485760 \
     -Dskywalking.plugin.toolkit.log.grpc.reporter.upstream_timeout=30 \
     ahao.jar --server.port=8080
