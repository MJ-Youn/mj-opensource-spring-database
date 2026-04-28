# mj-opensource-spring-web

- Spring에서 JPA를 사용하는 프로젝트를 진행하면서 필요한 기능을 추가 개발하는 유틸용 library

# How To Use

## release note

### 0.1.0 - 20231108

- JpaCRUDService 추가

### 0.1.1 - 20240325

- log 출력을 위한 class 이름 변경 (-> short)

### 0.1.2 - 20250218

- 내부 라이브러리 버전 최신화
- 배포 library 변경

* nexus-staging-maven-plugin -> central-publishing-maven-plugin
* repository 위치 변경 (ossrh -> central)
* license, developer info 추가

### 0.1.3 - 20250522

- logger 설정 변경
- JpaCRUDService#convertEntityList2DTOList 함수 생성

### 0.1.4 - 20250702

- SimpleJpaCRUDService의 repository, object 접근제어자 변경

### 25.4.7-20260316.0 - 20260316

- java 버전 변경 (21 -> 25)
- spring-boot 버전 변경 (3.2.5 -> 4.0.3)
- spring-data-jdbc 버전 변경 (3.2.5 -> 4.0.3)

### 25.4.7-20260428.0 - 20260428

-jackson library 버전 변경 (2.13.1 -> 3.1.2)

### 25.4.7-20260428.1 - 20260428

- pom.xml 수정 (description)
