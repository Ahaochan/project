# 简介
使用`Spring`提供的`@EnableWebSocket`注解实现的`WebSocket`服务端, 以及原生`js`的客户端以及`sockjs`客户端.

实现步骤很简单
1. 实现`WebSocketConfigurer`接口, 并用`@EnableWebSocket`修饰, 添加消息处理`Handler`
1. 消息处理`Handler`继承自`TextWebSocketHandler`类.