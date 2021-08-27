local cjson = require("cjson");
local producer = require("resty.kafka.producer");
local http = require("resty.http")
local template = require("resty.template");

-- 1. 获取id参数
local uri_args = ngx.req.get_uri_args();
local id = uri_args["id"];
if(id == nil or id == '') then
    ngx.say("error argument, id is empty");
    return;
end

-- 2. 上报kafka
local broker_list = {
    { host = "192.168.19.128", port = 19092 }
}

local log_json = {
    headers = ngx.req.get_headers(),
    uri_args = ngx.req.get_uri_args(),
    body = ngx.req.read_body(),
    http_version = ngx.req.http_version(),
    method = ngx.req.get_method(),
    raw_header = ngx.req.raw_header(),
    body_data = ngx.req.get_body_data()
};
local message = cjson.encode(log_json);
local product_id = ngx.req.get_uri_args()["id"];
local bp = producer:new(broker_list, { producer_type = "async"})
local ok, err = bp:send("access-log-topic", product_id, message); -- 以product_id为key, 往access-log-topic发送message数据
if not ok then
    ngx.log(ngx.ERR, "kafka send err:", err);
end

-- 3. 获取缓存
local cache_ngx = ngx.shared.product_cache;
local product_cache_key = "product_info_"..id;
local product_cache = cache_ngx:get(product_cache_key);
ngx.log(ngx.INFO, "================> cache: ", product_cache);
if product_cache == "" or product_cache == nil then
    local httpc = http.new();
    local resp, err = httpc:request_uri("http://127.0.0.1:80", {
        method = "GET",
        path = "/productJava?id="..id,
    })
    product_cache = resp.body;
    ngx.log(ngx.INFO, "================> load data: ", product_cache);
    cache_ngx:set(product_cache_key, product_cache, 10);
end

-- 4. 解析json
local product_json = cjson.decode(product_cache);

local context = {
    id = product_json.id
}

-- 5. 渲染模板
template.render("product.html", context);
