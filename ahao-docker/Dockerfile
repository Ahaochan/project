FROM registry.cn-hangzhou.aliyuncs.com/acs/maven:3-jdk-8 AS build
WORKDIR /usr/app
COPY pom.xml /usr/app
RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "verify", "clean", "--fail-never"]
COPY src /usr/app/src
RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "verify", "-DfinalName=ahao"]

FROM java:8-jre-alpine
COPY --from=build /usr/app/target/ahao.jar /usr/app/app.jar
ENTRYPOINT ["/usr/bin/java", "-jar"]
CMD ["/usr/app/app.jar"]
# docker build -t ahao-docker .
# docker run -it ahao-docker
