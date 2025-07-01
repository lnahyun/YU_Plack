# Plack
**오픈소스SW설계 과목 레포지토리** <br>

- **plan**과 **stack**을 결합한 웹 기반 일정 관리 서비스로, 일정 완료 시 점수를 획득하고 누적 점수에 따라 레벨이 상승하는 시스템입니다.

![Image](https://github.com/user-attachments/assets/bddc44e1-8765-4eff-8583-d0f1feef8ac4) <br>

## 기술 스택
**Backend** <br>
- Java 17
- SpringBoot 3.x
- Spring Data JPA (Hibernate)
- MySQL
- Thymeleaf

**Frontend**
- HTML / CSS / JS (Thymeleaf 기반)
- Flatpickr

**Infra**
- AWS EC2 (배포 서버)
- MySQL (AWS 서버 내 설치)

## 배포 주소 
### (2025.07.02 서버 종료) <br>
기본 화면은 **회원가입 페이지**이며, 로그인에 성공해 **세션이 등록된 경우**에만 홈 화면으로 진입할 수 있습니다.
```
http://43.200.6.117:8080/users/register
```
※ 간혹 서버 상태에 따라 접속이 원활하지 않을 수 있어, 로컬 실행 방법도 함께 제공합니다. <br>


## 로컬 실행 방법
**Java 17 및 Gradle 환경 확인**
- Java 17 이상 설치 필요
- Gradle Wrapper(./gradlew) 포함되어 있음

**1. GitHub에서 프로젝트 클론**
```bash
git clone https://github.com/lnahyun/YU_Plack.git
cd YU_Plack
```

**2. MySQL 실행 및 연동** <br>
로컬DB 설정을 ```.yml```에서 다음과 같이 맞춰주세요.
```bash
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/plackdb
    username: [username]
    password: [비밀번호]
```

**3. 실행**
```bash
./gradlew bootRun
```

**4. 접속 확인**
```bash
http://localhost:8080/users/register
```

