#!/bin/bash
# https://www.rancher.com/quick-start

docker run --name rancher --privileged \
    -d --restart=unless-stopped \
    -p 1080:80 -p 1443:443 \
    -v /opt/rancher:/var/lib/rancher/ \
    rancher/rancher:v2.4.8

curl 127.0.0.1:1080
