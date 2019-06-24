# 简介
本 `Demo` 基于 `WxJava`.
暴露接口给微信的部分需要启动服务, 并做内网穿透.
不接收微信数据的部分, 可以在单元测试中完成, 不用启用服务和内网穿透

# 使用方法
1. 使用内网穿透工具, 比如[`natapp`](https://natapp.cn/login)
1. 注册一个[微信测试号](https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login)
1. 修改接口配置信息, `token`随便写, `url`必须写成`http://内网穿透后的域名/wx/portal/测试号的appID`
1. 修改`application.yml`, 加上自己的公众号配置.
1. 根据需要, 配置`profile`, 启动 `Starter`.
