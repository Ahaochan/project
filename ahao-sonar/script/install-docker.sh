#!/bin/bash
# https://hub.docker.com/_/sonarqube/
docker run -d --name sonarqube -p 9000:9000 sonarqube
# 安装中文插件
# http://ip:9000/admin/marketplace?search=chinese
