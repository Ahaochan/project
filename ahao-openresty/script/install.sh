#!/bin/bash
# 1. 安裝依赖
apt-get install libpcre3-dev libssl-dev perl make build-essential curl zlib1g-dev -y

# 2. 安装 OpenResty
VERSION=1.19.3.1
wget https://openresty.org/download/openresty-${VERSION}.tar.gz
tar xzf openresty-${VERSION}.tar.gz
cd openresty-${VERSION} || return 1

cd bundle/LuaJIT* || return 1
make clean && make && make install
cd - || return 1

cd bundle/ || return 1
wget https://github.com/FRiCKLE/ngx_cache_purge/archive/refs/tags/2.3.tar.gz
tar xzf 2.3.tar.gz
wget https://github.com/yaoweibin/nginx_upstream_check_module/archive/refs/tags/v0.3.0.tar.gz
tar xzf v0.3.0.tar.gz
cd - || return 1

./configure --prefix=/opt/openresty \
            --with-http_realip_module \
            --with-pcre \
            --with-luajit \
            --add-module=./bundle/ngx_cache_purge-2.3/ \
            --add-module=./bundle/nginx_upstream_check_module-0.3.0/ \
            -j2
make && make install

# 3. 启动项目
/opt/openresty/nginx/sbin/nginx -V
/opt/openresty/nginx/sbin/nginx -t -c /opt/openresty/nginx/conf/nginx.conf
/opt/openresty/nginx/sbin/nginx -s reload

curl 127.0.0.1/hello

# 4. 下载http lib
cd lualib/resty || return 1
wget https://raw.githubusercontent.com/ledgetech/lua-resty-http/master/lib/resty/http.lua
wget https://raw.githubusercontent.com/ledgetech/lua-resty-http/master/lib/resty/http_headers.lua
cd - || return 1
/opt/openresty/nginx/sbin/nginx -s reload

# 5. 下载模板渲染lib
cd lualib/resty || return 1
wget https://raw.githubusercontent.com/bungle/lua-resty-template/master/lib/resty/template.lua
mkdir html && cd html || return 1
wget https://raw.githubusercontent.com/bungle/lua-resty-template/master/lib/resty/template/html.lua
cd - && cd - || return 1
/opt/openresty/nginx/sbin/nginx -s reload
