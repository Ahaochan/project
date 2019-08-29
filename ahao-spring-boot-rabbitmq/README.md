# 简介
简单的`RabbitMQ`使用, 集成了`fastjson`消息转化器

1. 启动`RabbitMQ`, 并配置`IP`
1. 下载安装[官方延迟插件](https://github.com/rabbitmq/rabbitmq-delayed-message-exchange)
1. 运行单元测试

注意, `fastjson`消息转换器不支持含有泛型的对象转换.
建议直接使用原生`json`字符串.
