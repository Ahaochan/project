#!/bin/bash
wget https://raw.githubusercontent.com/liuyangming/ByteTCC/master/bytetcc-supports/src/main/resources/bytetcc.sql
mysql -h 127.0.0.1 -P 3306 -u root -proot < bytetcc.sql
