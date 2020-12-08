FROM openjdk:11
LABEL maintainer="chernanq@gmail.com"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]