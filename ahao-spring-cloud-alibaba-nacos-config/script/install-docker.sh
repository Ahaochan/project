#!/bin/bash
docker run --name nacos-standalone -e MODE=standalone -p 8848:8848 -p 9848:9848 -p 9849:9849 -d nacos/nacos-server:latest
# 控制台地址: http://127.0.0.1:8848/nacos/
# 连接地址:   127.0.0.1:8848
