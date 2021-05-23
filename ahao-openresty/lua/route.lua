local uri_args = ngx.req.get_uri_args();
local id = uri_args["id"];

local hosts = {"127.0.0.1", "127.0.0.1"}
local hash = ngx.crc32_long(id);
local index = hash % #hosts + 1;
local backend = "http://"..hosts[index];

local requestPath = "/"..uri_args["method"].."?id="..id;

local http = require("resty.http")
local httpc = http.new();

local resp, err = httpc:request_uri(backend, {
    method = "GET",
    path = requestPath,
})

if not resp then
    ngx.say("request error: ", err);
    return;
end

ngx.say(resp.body);
httpc.close();
