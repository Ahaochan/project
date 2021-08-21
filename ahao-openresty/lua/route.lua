-- 1. 获取id参数
local uri_args = ngx.req.get_uri_args();
local id = uri_args["id"];
if(id == nil or id == '') then
    ngx.say("error argument, id is empty");
    return;
end

-- 2. 路由到某一个后端
local hosts = {"127.0.0.1:81", "127.0.0.1:82"}
local hash = ngx.crc32_long(id);
local index = hash % #hosts + 1;
local backend = "http://"..hosts[index];
local requestUri = "/"..ngx.var.request_uri; -- 获取uri和参数
ngx.log(ngx.INFO, "id", id, ", backend:", backend)

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
