local uri_args = ngx.req.get_uri_args();
local id = uri_args["id"];
local flag = uri_args["flag"];

local cache_ngx = ngx.shared.product_cache;
local product_hot_cache_key = "product_hot_"..id;
cache_ngx:set(product_hot_cache_key, flag, 60 * 60);
ngx.say('热门商品', id, ', flag=', flag);
