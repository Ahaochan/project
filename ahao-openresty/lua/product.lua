-- 1. 获取id参数
local uri_args = ngx.req.get_uri_args();
local id = uri_args["id"];
if(id == nil or id == '') then
    ngx.say("error argument, id is empty");
    return;
end

-- 2. 获取缓存
local cache_ngx = ngx.shared.product_cache;
local product_cache_key = "product_info_"..id;
local product_cache = cache_ngx:get(product_cache_key);
ngx.log(ngx.INFO, "================> cache: ", product_cache);
if product_cache == "" or product_cache == nil then
    local http = require("resty.http")
    local httpc = http.new();
    local resp, err = httpc:request_uri("http://127.0.0.1:80", {
        method = "GET",
        path = "/productJava?id="..id,
    })
    product_cache = resp.body;
    ngx.log(ngx.INFO, "================> load data: ", product_cache);
    cache_ngx:set(product_cache_key, product_cache, 10);
end

-- 3. 解析json
local cjson = require("cjson");
local product_json = cjson.decode(product_cache);

local context = {
    id = product_json.id
}

-- 4. 渲染模板
local template = require("resty.template");
template.render("product.html", context);
