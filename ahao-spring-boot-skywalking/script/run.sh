#!/bin/bash
java -jar -javaagent:/opt/skywalking-agent.jar \
     -Dskywalking.agent.service_name=AhaoWeb \
     -Dskywalking.collector.backend_service=127.0.0.1:11800 \
     ahao.jar --server.port=8080
