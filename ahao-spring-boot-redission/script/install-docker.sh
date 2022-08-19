#!/bin/bash
docker run --name redis -d -p 6379:6379 \
 --restart always \
 redis --requirepass "root"
