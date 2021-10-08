local uri_args = ngx.req.get_uri_args();
local id = uri_args["id"];
local flag = uri_args["flag"];

local cache_ngx = ngx.shared.product_cache;
local product_hot_cache_key = "product_hot_"..id;
math.randomseed(tostring(os.time()):reverse():sub(1, 7))
local expire_time = math.random(600, 1200)
cache_ngx:set(product_hot_cache_key, flag, expire_time);
ngx.say('热门商品', id, ', flag=', flag);
