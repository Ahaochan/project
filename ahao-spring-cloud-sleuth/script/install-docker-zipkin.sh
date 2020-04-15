#!/bin/bash
docker run -d -p 9411:9411 openzipkin/zipkin
echo `hostname -I | awk '{print $1}'`:9411
