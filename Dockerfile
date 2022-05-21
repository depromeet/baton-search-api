# 배포용 Dockerfile 입니다.

FROM openjdk:11-jre-slim
ARG JAR_FILE=build/libs/baton-search-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-DDB_URL=${DB_URL}","-DDB_USER=${DB_USER}","-DDB_PASSWORD=${DB_PASSWORD}","-DS3_ACCESS_KEY=${S3_ACCESS_KEY}","-DS3_SECRET_KEY=${S3_SECRET_KEY}","-DAWS_BUCKET=${AWS_BUCKET}","-DAWS_FOLDER=${AWS_FOLDER}","/app.jar"]