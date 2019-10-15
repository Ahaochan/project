# Docker file 需要移动到父模块下, 并且此 Dockefile 生成镜像耗时长, 多在下载依赖
# TODO 运行错误, 可能是 jre 版本问题
# mv ./ahao-spring-cloud-eureka/Dockerfile ./Dockerfile
# docker build -t tomcat-eureka .
# docker run -d -p 8761:8761 -e PROFILE=server tomcat-eureka
FROM maven:3-jdk-8-alpine as builder
VOLUME /tmp
COPY .. /app
WORKDIR /app
RUN mvn verify clean --fail-never \
    && mvn clean package -pl ahao-spring-cloud-eureka -am \
    && mv ahao-spring-cloud-eureka/target/ahao-spring-cloud-eureka-1.0.0.jar /eureka.jar

FROM openjdk:8u181-jre-alpine3.8 as environment
ENV PROFILE server
WORKDIR /
COPY --from=builder /eureka.jar .
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=$PROFILE}", "-jar", "/eureka.jar"]
