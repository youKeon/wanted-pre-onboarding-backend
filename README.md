# wanted-pre-onboarding-backend

## 지원자
- [유건](https://github.com/youKeon)

<br>

## 클라우드 배포 주소
- `52.78.231.54`

<br>

## AWS 아키텍처
![image](https://github.com/youKeon/wanted-pre-onboarding-backend/assets/96862049/6de8014a-b578-4e9b-91e2-c44f2b4b9138)

## 실행 방법
1. build
```java
$ ./gradlew build
```

2. docker-compose 실행
```java
$ sudo docker-compose up -d
```


<br>

## ERD
<img width="587" alt="스크린샷 2023-08-11 20 45 51" src="https://github.com/youKeon/wanted-pre-onboarding-backend/assets/96862049/e929cbbf-b6f3-4a04-bf47-d79d522d8e2b">

- Member(사용자)와 Post(게시글)의 관계를 1:N 관계로 설정

<br>

## API 명세서
- [REST Docs 기반 API 명세서](http://52.78.231.54:8080/)
- [Swagger API 명세서](http://52.78.231.54:8080/swagger-ui/index.html#/)
<br>

## API 데모 영상
- [데모 영상 링크](https://drive.google.com/file/d/1IjHXXlYGxoqXKqX4uyL0hyr4FEiZLp9t/view?usp=drive_link)

## 구현 설명
### `회원`

- **회원가입**
  - 회원가입 시 이메일 형식을 검사
  - 이메일과 비밀번호에 공백이 입력되면 예외 발생

<br>

- **로그인**
    - 회원가입과 동일하게 이메일과 비밀번호의 유효성 검사
    - 입력값이 유효하면 DB에 인코딩된 비밀번호와 동일한지 검사
    - 동일하면 Access Token 발급

<br>

### `게시글`
- **게시글 생성**
    - 회원가입과 동일하게 이메일과 비밀번호의 유효성 검사
    - 햔재 로그인된 사용자의 정보를 바탕으로 유효한 사용자인지 검사
    - 요청으로 들어온 게시판 내용을 DB에 저장

<br>

- **게시글 단건 조회**
  - 입력받은 게시글 ID로 존재하는 조회하려는 게시글의 존재 여부 검사
  - 존재하는 경우 DTO로 매핑해서 반환

<br>

- **게시글 전체 조회**
  - 입력받은 페이지 수와 사이즈만큼 조회
  - 조회된 게시글 개수가 0개이면 예외 발생
  - Response에 사용되는 DTO에는 마지막 페이지인지 확인하는 boolean 타입의 속성 `isLastPage` 추가

<br>

- **게시글 수정**
  - 입력 받은 게시글 ID로 실제 존재하는 게시글인지 확인
  - 실제 존재하는 게시글인 경우 게시글과 연관된 사용자를 `JOIN FETCH`를 사용하여 한 번에 조회
  - 게시글과 함께 조회된 사용자 객체로 유효한 사용자인지(실제 존재하는 사용자인지, 작성자와 동일한 사용자인지) 검사

<br>

- **게시글 삭제**
  - `게시글 수정`과 동일
