#!/bin/bash
docker run --name my-redis -d -p 6379:6379 redis --requirepass "root"
