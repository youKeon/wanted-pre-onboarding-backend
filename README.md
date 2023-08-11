# wanted-pre-onboarding-backend

## 지원자
- [유건](https://github.com/youKeon)

<br>

## 실행 방법
1. build
```java
$ ./gradlew build jar -x test
```

2. docker 실행
```java
$ docker-compose up --build -d
```


<br>

## ERD
![스크린샷 2023-08-10 21.20.45.png](..%2F..%2F..%2F..%2F..%2Fvar%2Ffolders%2F00%2F68jpnmhx1qb3745ll7gz5c4r0000gn%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_ohEo5N%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202023-08-10%2021.20.45.png)
- Member(사용자)와 Post(게시글)의 관계를 1:N 관계로 설정

<br>

## API 명세서
[- 링크
](https://shell-pancreas-3d6.notion.site/API-Docs-d7cc86b3b2784db493a46b391a246165?pvs=4)
<br>

## API 데모 영상


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