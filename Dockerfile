# 배포용 Dockerfile 입니다.

FROM openjdk:11-jdk
ARG JAR_FILE=build/libs/baton-search-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-DDB_URL=${DB_URL}","-DDB_USER=${DB_USER}","-DDB_PASSWORD=${DB_PASSWORD}","/app.jar"]