# 简介
本项目是本人自用的一套 `Java` 代码库.

# 项目结构
- ahao-all
  - ahoa-spring-cloud-eureka (服务注册中心 Demo)
  - ahao-web (Web项目通用代码)
  - ~~ahao-forum-guitar (论坛项目, 放弃维护)~~
  - ~~ahao-invoice (企业增值税数据分析系统, 放弃维护)~~
  
# 常用功能
1. 阿里银行数据接口: `com.ahao.web.module.alipay.bank`
1. 文件上传: `com.ahao.web.module.upload`
1. 负载均衡的主从数据源: `com.ahao.spring.jdbc`
1. 异步任务: `com.ahao.spring.async`
1. `Redis`结合`AOP`实现缓存层: `com.ahao.redis`
  
# TODO
- docker化应用