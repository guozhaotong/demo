FROM openjdk:8-jdk-alpine
RUN mkdir -p /root/workspace/project
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime
WORKDIR /root/workspace/project
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java",\
            "-Djava.security.egd=file:/dev/./urandom",\
            "-Dspring.profiles.active=pro",\
            "-jar",\
            "/root/workspace/project/app.jar"]
