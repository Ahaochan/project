FROM tomcat
RUN rm -rf /usr/local/tomcat/webapps/*
COPY ./ROOT.war /usr/local/tomcat/webapps/
ENTRYPOINT startup.sh && tail -fn 300 /usr/local/tomcat/logs/catalina.out
# docker build -t tomcat-eureka .
# docker run -d -p 8761:8080 tomcat-eureka