分布式日志打印实现

# 日志配置加载流程
`Spring Cloud`和`Spring Boot`是两个不同的`Context`, 初始化的时候会分别初始化事件监听器, 完成事件监听流程.
所以需要在`bootstrap.yml`和`application.yml`都配置上`logging.config`配置.

```java
// org.springframework.boot.context.logging.LoggingApplicationListener
public class LoggingApplicationListener implements GenericApplicationListener {
    private void initializeSystem(ConfigurableEnvironment environment, LoggingSystem system, LogFile logFile) {
        LoggingInitializationContext initializationContext = new LoggingInitializationContext(environment);
        // application.yml 加载一次, bootstrap.yml 加载一次
        String logConfig = environment.getProperty("logging.config");
        if (str == null || str.isEmpty() || logConfig.startsWith("-D")) {
            system.initialize(initializationContext, null, logFile); // 重点
        } else {
            ResourceUtils.getURL(logConfig).openStream().close();
            system.initialize(initializationContext, logConfig, logFile); // 重点
        }
    }
}
```
最终是调用了`LoggingSystem`的`initialize`方法. 以`Logback`的实现为例
```java
// org.springframework.boot.logging.logback.AbstractLoggingSystem
public abstract class AbstractLoggingSystem extends LoggingSystem {
    private void initializeWithConventions(LoggingInitializationContext initializationContext, LogFile logFile) {
        // 1. logback 尝试加载 logback-test.groovy, logback-test.xml, logback.groovy, logback.xml
        //    log4j2  尝试加载 log4j2.properties, log4j2.yaml, log4j2.yml, log4j2.json, log4j2.jsn, log4j2.xml
        String config = getSelfInitializationConfig();
        if (config != null && logFile == null) {
            // self initialization has occurred, reinitialize in case of property changes
            reinitialize(initializationContext);
            return;
        }
        // 2. 尝试加上 -spring 加载 spring 日志配置文件
        //    logback 尝试加载 logback-test-spring.groovy, logback-test-spring.xml, logback-spring.groovy, logback-spring.xml
        //    log4j2  尝试加载 log4j2-spring.properties, log4j2-spring.yaml, log4j2-spring.yml, log4j2-spring.json, log4j2-spring.jsn, log4j2-spring.xml
        if (config == null) {
            config = getSpringInitializationConfig();
        }
        if (config != null) {
            loadConfiguration(initializationContext, config, logFile);
            return;
        }
        // 3. 如果都没有, 就加载默认的日志配置
        loadDefaults(initializationContext, logFile);
    }
}
```

# Logback 

## Logback 使用 property标签 加载配置文件
我们可以看`PropertyAction`的代码
```java
// ch.qos.logback.core.joran.action.PropertyAction
public class PropertyAction extends Action {
    public void begin(InterpretationContext ec, String localName, Attributes attributes) {
        String name = attributes.getValue(NAME_ATTRIBUTE);
        String value = attributes.getValue(VALUE_ATTRIBUTE);
        String scopeStr = attributes.getValue(SCOPE_ATTRIBUTE);

        Scope scope = ActionUtil.stringToScope(scopeStr);
        if (checkFileAttributeSanity(attributes)) { // 只有 file 属性
            String file = attributes.getValue(FILE_ATTRIBUTE);
            file = ec.subst(file);
            FileInputStream istream = new FileInputStream(file);
            loadAndSetProperties(ec, istream, scope);
        } else if (checkResourceAttributeSanity(attributes)) { // 只有 resource 属性
            String resource = attributes.getValue(RESOURCE_ATTRIBUTE);
            resource = ec.subst(resource);
            URL resourceURL = Loader.getResourceBySelfClassLoader(resource);
            InputStream istream = resourceURL.openStream();
            loadAndSetProperties(ec, istream, scope);
        } else if (checkValueNameAttributesSanity(attributes)) { // 只有 name 或 value 属性
            value = RegularEscapeUtil.basicEscape(value);
            value = value.trim();
            value = ec.subst(value);
            ActionUtil.setProperty(ec, name, value, scope);
        }
    }

    void loadAndSetProperties(InterpretationContext ec, InputStream istream, Scope scope) throws IOException {
        Properties props = new Properties(); // 重点!!! 只能加载 properties 文件
        props.load(istream);
        istream.close();
        ActionUtil.setProperties(ec, props, scope);
    }

}
```
总结下, `property`标签加载配置文件有以下规则
1. 必须为`properties`文件, 不支持`yml`和`json`
2. 标签属性只有三种组合, `file`、`resource`、`name`或`value`, 否则会有异常
