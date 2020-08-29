#!/bin/bash
# https://www.elastic.co/guide/en/kibana/current/docker.html

YOUR_ELASTICSEARCH_CONTAINER_NAME_OR_ID=
docker pull docker.elastic.co/kibana/kibana:7.8.1
docker run --link ${YOUR_ELASTICSEARCH_CONTAINER_NAME_OR_ID}:elasticsearch -p 5601:5601 kibana:7.8.1
