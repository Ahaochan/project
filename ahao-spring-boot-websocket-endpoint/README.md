# 简介
使用`Java`提供的`@ServerEndpoint`注解实现的`WebSocket`服务端, 以及原生`js`的客户端.

实现步骤很简单
1. 注册一个`ServerEndpointExporter`的`Bean`
1. 在服务请求处理类添加一个`@ServerEndpoint("/chat")`, 并为回调函数添加`@OnXxx`注解.

# 注意
被`@ServerEndpoint`修饰的类, 每次`WebSocket`请求`Open`都会创建一个对象, 所以可能会出现`@Autowired`失效的情况.
解决方案有两种
```java
@Component
@ServerEndpoint("/chat")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    
    // 1. 用 static 修饰, 并添加 setter
    private static ChatService chatService;
    @Autowired
    public void setChatService(ChatService chatService) {
        ChatController.chatService = chatService;
    }

    // 2. 直接使用 ApplicationContext 获取
    public ChatService getChatService() {
        return SpringContextHolder.getBean(ChatService.class);
    }
}
```