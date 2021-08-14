#!/bin/bash
# 0 * * * * sh /usr/local/redis/copy/redis_rdb_copy_hourly.sh /var/redis/6379/dump.rdb /usr/local/redis/snapshotting
bak_dir=$2
rdb_path=$1
if [ ! -f "$rdb_path" ]; then
    echo "$rdb_path不存在! 无法进行备份!";
    exit 1;
fi

cur_yyyyMMddHH=$(date +%Y%m%d%k)
rm -rf "${bak_dir:=/}/$cur_yyyyMMddHH"
mkdir "${bak_dir:=/}/$cur_yyyyMMddHH"
cp "$rdb_path" "${bak_dir:=/}/$cur_yyyyMMddHH"

before_yyyyMMddHH=$(date -d -48hour +%Y%m%d%k)
rm -rf "${bak_dir:=/}/$before_yyyyMMddHH"
