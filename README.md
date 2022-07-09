# Baton Search API Server

[Google Play에서 바통 다운로드](https://play.google.com/store/apps/details?id=com.depromeet.baton)

![banner](https://user-images.githubusercontent.com/86508420/176690477-a0d002fc-ce84-4820-a3a9-daaba24d9eb6.png)

디프만 11기 6조 바통 어플리케이션의 검색, 양도권 등록 및 FCM API Server 입니다.

## Background

### Architecture

![API Flowpng](https://user-images.githubusercontent.com/86508420/176701877-78c21e38-c2e5-40d5-a815-fd0a9ddc358a.png)
![인프라](https://user-images.githubusercontent.com/86508420/176702393-281778aa-46cd-4815-aeb1-72a971b371a5.png)


### Database Schema

![DBSchema_light](https://user-images.githubusercontent.com/86508420/176701610-721ab6c0-8a22-41e3-9ab9-b33b4fb6e881.png)


### CI/CD

![CICD](https://user-images.githubusercontent.com/86508420/176702474-614a02fa-a296-4ab1-9e06-eb8de6a6e9ea.png)

## Technology Stack

<div>

![Spring](https://img.shields.io/badge/-Spring-6DB33F?logo=Spring&logoColor=white&style=flat)
![Spring Boot](https://img.shields.io/badge/-Spring--Boot-6DB33F?logo=Spring%20Boot&logoColor=white&style=flat)
![Firebase](https://img.shields.io/badge/-Firebase-FFCA28?logo=Firebase&logoColor=black&style=flat)
![MySQL](https://img.shields.io/badge/-MySQL-blue?logo=MySQL&logoColor=white&style=flat)
![Swagger](https://img.shields.io/badge/-Swagger-a4ff82?logo=Swagger&logoColor=black&style=flat)
![Docker](https://img.shields.io/badge/-Docker-2496ED?logo=Docker&logoColor=white&style=flat)
![Github--Actions](https://img.shields.io/badge/-Github--Actions-0006ff?logo=GitHub%20Actions&logoColor=white&style=flat)
![AWS S3](https://img.shields.io/badge/-AWS%20S3-569A31?logo=Amazon%20S3&logoColor=white&style=flat)

</div>

## Main Feature
- **실제 유저들이 자주 사용하는 조건을 인덱싱** 하여 개선한 SQL 쿼리 실행계획.
- 위를 바탕으로, **QueryDSL**을 이용 한, **양도권 다중 검색** 기능 구현.
- **Custom Dialect**를 이용, JPA 단에서 **MySQL** 함수 사용 하여, 위치 검색 구현.
- 양도권 검색 시 **JOIN문 없는 빠른 쿼리**를 위해 **테이블 비정규화** 실시.
- S3에 이미지 저장 시, 원본 이미지와, 이를 **썸네일 화** 시킨 이미지를 저장, 모바일 환경 최적화.
- **Firebase Message API** 구현
- 협업을 위해, **Swagger**를 사용

## See more

* [Baton](https://github.com/depromeet/Baton) (Android)
* [baton-user-api](https://github.com/depromeet/baton-user-api) (Django)
* [baton-chat-server](https://github.com/depromeet/baton-chat-server) (Go)
* [baton-infra](https://github.com/depromeet/baton-infra)

### Download at
![QR코드](https://user-images.githubusercontent.com/86508420/176703343-2a5030ba-f30c-407d-af3b-1797681bcaf7.png)
