package com.ahao.spring.cloud.zuul.enums;

public enum  ZuulFilterTypeEnum {
    PRE("pre", "这种过滤器在请求被路由之前调用. 我们可利用这种过滤器实现身份验证、在集群中选择请求的微服务、记录调试信息等."),
    ROUTE("route", "这种过滤器将请求路由到微服务. 这种过滤器用于构建发送给微服务的请求, 并使用Apache HttpClient或Netfilx Ribbon请求微服务."),
    POST("post", "这种过滤器在路由到微服务以后执行. 这种过滤器可用来为响应添加标准的HTTP Header、收集统计信息和指标、将响应从微服务发送给客户端等."),
    ERROR("error", "在其他阶段发生错误时执行该过滤器.");

    String filterType;
    String message;

    ZuulFilterTypeEnum(String filterType, String message) {
        this.filterType = filterType;
        this.message = message;
    }

    public String getFilterType() {
        return filterType;
    }

    public String getMessage() {
        return message;
    }
}
