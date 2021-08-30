-- 1. 获取id参数
local uri_args = ngx.req.get_uri_args();
local id = uri_args["id"];
if(id == nil or id == '') then
    ngx.say("error argument, id is empty");
    return;
end

-- 2. 如果是热点数据, 就随机路由, 如果不是热点数据, 就固定路由到某一个后端
local hosts = {"127.0.0.1:81", "127.0.0.1:82"}

local cache_ngx = ngx.shared.product_cache;
local product_hot_cache_key = "product_hot_"..id;
local product_hot_flag = cache_ngx:get(product_hot_cache_key);
local index = 0;
if product_hot_flag == 'true' then
    math.randomseed(tostring(os.time()):reverse():sub(1, 7));
    index = math.random(1, #hosts);
    ngx.log(ngx.INFO, "热点商品, 随机路由到", hosts[index]);
else
    local hash = ngx.crc32_long(id);
    index = hash % #hosts + 1;
    ngx.log(ngx.INFO, "非热点商品, 固定路由到", hosts[index]);
end
local backend = "http://"..hosts[index];
local requestUri = "/"..ngx.var.request_uri; -- 获取uri和参数
ngx.log(ngx.INFO, "backend", backend, ", requestUri:", requestUri);

-- 3. 发起http请求
local http = require("resty.http")
local httpc = http.new();
local resp, err = httpc:request_uri(backend, {
    method = "GET",
    path = requestUri,
})
if not resp then
    ngx.say("request error: ", err);
    return;
end

ngx.say(resp.body);
httpc:close();
