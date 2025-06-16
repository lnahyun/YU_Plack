- 배포 주소
기본 화면은 회원가입 페이지이며, 로그인에 성공해 세션이 등록된 경우에만 홈 화면으로 진입할 수 있습니다.

http://43.200.6.117:8080/users/register

※ 간혹 서버 상태에 따라 접속이 원활하지 않을 수 있어, 로컬 실행 방법도 함께 제공합니다.

- 로컬 실행 방법
0. Java 17 및 Gradle 환경 확인
Java 17 이상 설치 필요
Gradle Wrapper(./gradlew) 포함되어 있음

1. GitHub에서 프로젝트 클론

git clone https://github.com/lnahyun/YU_Plack.git
cd YU_Plack

2. MySQL 실행 및 연동
로컬DB 설정을 .yml에서 다음과 같이 맞춰주세요.

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/plackdb
    username: [username]
    password: [비밀번호]

3. 실행

./gradlew bootRun

4. 접속 확인

http://localhost:8080/users/login