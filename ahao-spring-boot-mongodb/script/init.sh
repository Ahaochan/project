#!/bin/bash
# 连接数据库
mongosh --host 127.0.0.1 --port 27017 -u root -p root admin
# 创建数据库ahaodb
use ahaodb
# 为ahaodb创建用户
# db.createUser({user: 'ahao', pwd: 'ahao', roles: [{role: 'readWrite', db: 'ahaodb'}]})
