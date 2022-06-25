# 배포용 Dockerfile 입니다.

FROM openjdk:11-jre-slim
ARG JAR_FILE=build/libs/baton-search-0.0.1-SNAPSHOT.jar
ARG db_url
ARG db_user
ARG db_password
ARG s3_access_key
ARG s3_secret_key
ARG aws_bucket
ARG aws_folder
ARG fcm_url
ARG fcm_key

ENV DB_URL=${db_url}
ENV DB_USER=${db_user}
ENV DB_PASSWORD=${db_password}
ENV S3_ACCESS_KEY=${s3_access_key}
ENV S3_SECRET_KEY=${s3_secret_key}
ENV AWS_BUCKET=${aws_bucket}
ENV AWS_FOLDER=${aws_folder}
ENV FCM_URL=${fcm_url}
ENV FCM_KEY=${fcm_key}
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","-Dspring.datasource.url=jdbc:mysql://${DB_URL}/baton","-Dspring.datasource.username=${DB_USER}","-Dspring.datasource.password=${DB_PASSWORD}","-Dcloud.aws.credentials.access-key=${S3_ACCESS_KEY}","-Dcloud.aws.credentials.secret-key=${S3_SECRET_KEY}","-Dcloud.aws.s3.bucket=${AWS_BUCKET}","-Dcloud.aws.s3.prefix=${AWS_FOLDER}","-Dfirebase.url=${FCM_URL}", "-Dfirebase.key=${FCM_KEY}", "/app.jar"]
