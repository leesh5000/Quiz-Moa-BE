# quiz-app-be
퀴즈 애플리케이션 백엔드 프로젝트

## 사용 기술

- Java 17
- Gradle 7.5.1
- [Spring Boot 3.0.1](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Rest Docs](https://spring.io/projects/spring-restdocs)
- [jjwt](https://github.com/jwtk/jjwt)
- [Asciidoctor](https://docs.asciidoctor.org/)
- [H2 Database](https://www.h2database.com/html/main.html)

## 유즈케이스

[공유 링크](https://viewer.diagrams.net/?tags=%7B%7D&highlight=0000ff&edit=_blank&layers=1&nav=1&title=usecase.svg#Uhttps%3A%2F%2Fraw.githubusercontent.com%2Fleesh5000%2Fquiz-app-be%2Fdocument%2F%252329-readme%2Fdocument%2Fusecase.svg)

![usecase](document/usecase.svg)

## DB 테이블 설계

[공유 링크](https://viewer.diagrams.net/?tags=%7B%7D&highlight=0000ff&edit=_blank&layers=1&nav=1&title=erd.svg#Uhttps%3A%2F%2Fraw.githubusercontent.com%2Fleesh5000%2Fquiz-app-be%2Fdocument%2F%252329-readme%2Fdocument%2Ferd.svg)

![erd](document/erd.svg)

### 테이블 스키마

```mysql
use mysql;
select host, user from user;

select @@global.time_zone, @@session.time_zone,@@system_time_zone;

create database if not exists quizapp default character set utf8mb4 collate utf8mb4_general_ci;

use quizapp;

set foreign_key_checks = 0;
drop table if exists users cascade;
drop table if exists quiz cascade;
drop table if exists comment cascade;
drop table if exists vote cascade;
set foreign_key_checks = 1;

create table users
(
    id          bigint auto_increment,
    nickname    varchar(30)  not null unique,
    email       varchar(255) not null unique,
    password    varchar(255) not null,
    created_at  bigint       not null,
    modified_at bigint       not null,
    primary key (id)
) default character set utf8mb4 collate utf8mb4_general_ci;

create index users_nickname_idx on users (nickname);
create index users_email_idx on users (email);
create index users_created_at_idx on users (created_at);

create table quiz
(
    id          bigint auto_increment,
    user_id     bigint         not null,
    title       varchar(255)   not null,
    contents    text not null,
    created_at  bigint         not null,
    modified_at bigint         not null,
    primary key (id),
    foreign key (user_id) references users (id)
) default character set utf8mb4 collate utf8mb4_general_ci;

create index quiz_title_idx on quiz (title);
create index quiz_created_at_idx on quiz (created_at);
create index quiz_user_id_idx on quiz (user_id);

create table comment
(
    id          bigint auto_increment,
    user_id     bigint         not null,
    quiz_id     bigint         not null,
    contents    text not null,
    created_at  bigint         not null,
    modified_at bigint         not null,
    primary key (id),
    foreign key (user_id) references users (id),
    foreign key (quiz_id) references quiz (id)
)  default character set utf8mb4 collate utf8mb4_general_ci;

create index comment_created_at_idx on comment (created_at);
create index comment_user_id_idx on comment (user_id);

create table likes
(
    id          bigint auto_increment,
    user_id     bigint not null,
    quiz_id     bigint null,
    comment_id  bigint null,
    created_at  bigint not null,
    modified_at bigint not null,
    primary key (id),
    foreign key (user_id) references users (id),
    foreign key (quiz_id) references quiz (id),
    foreign key (comment_id) references comment (id)
)   default character set utf8mb4 collate utf8mb4_general_ci;

create index likes_created_at_idx on likes (created_at);
create index likes_user_id_idx on likes (user_id);
```

#### 공통 요소
- id : PK는 값을 넉넉하게 사용하기 위해 bigint 타입으로 지정하였습니다. 또한, 보안적으로 손해를 보더라도 성능상의 이점을 얻기 위해 auto increment로 설정하였습니다.
- created_at, modified_at : 생성일, 수정일은 모든 테이블에 존재하는 메타데이터입니다. 애플리케이션 레벨에서 타임존을 고려하기 위해 unix time을 넣어주도록 bigint 타입으로 설정하였습니다.

#### 유저 테이블 
- nickname : 닉네임은 적당한 크기인 30으로 하였으며 유저를 식별할 수 있는 값이어야 하므로 unique 속성을 주었습니다.
- email : 이메일은 회원가입 및 로그인 시 유저가 입력하는 정보입니다.
- password : 비밀번호는 암호화까지 고려하여 255 크기를 설정하였습니다.
- username, email, created_at 은 검색이 빈번하게 발생하는 요소라고 생각되어 인덱스를 설정하였습니다.

#### 퀴즈 테이블
- title : 퀴즈의 제목은 넉넉한 값인 255으로 설정하였습니다.
- contents : varchar 타입과 text 타입을 고민했습니다. varchar가 테이블에 인라인 방식으로 저장되기 때문에 디스크에 대한 포인터를 갖고 있는 text 방식 보다 빠를 거라고 생각하였지만, 다음 링크를 보고 큰 차이가 없다고 생각하여 text 타입으로 설정하였습니다. [링크1](https://stackoverflow.com/questions/6404628/varchar-vs-text-in-mysql?noredirect=1&lq=1) [링크2](https://blog.programster.org/mysql-benchmarking-varchar-vs-text)
- 퀴즈 작성자와는 1:N 관계를 위해 외래키를 설정하였습니다.
- 제목과 생성일 필드는 검색이 빈번하게 될 것이라고 생각하여 인덱스 설정을 하였습니다.

#### 댓글 테이블
- contents : 퀴즈 테이블은 contents와 같은 이유로 text 타입을 설정하였습니다.
- 댓글 작성자, 퀴즈와의 1:N 관계를 위해 외래키를 설정하였습니다.

#### 투표 테이블
- value : 값 필드는 음수 값도 허용하며 적절한 크기인 int로 하였습니다.
- 투표는 댓글, 퀴즈 모두와 Optional:N 관계를 가질 수 있기 때문에 NULL 값을 허용하였습니다.
- 얼마 이상의 투표를 받은 퀴즈인지도 확인해야 할 수 있기 때문에 검색을 위해 인덱스를 설정하였습니다.

## API 명세서

API 명세서는 다음 [파일](document/api-spec.xlsx)에서 확인하실 수 있습니다. 또는 해당 [공유 링크](https://docs.google.com/spreadsheets/d/1I_l4VhwKM8RCEsLkSF-ePsi_1EWj8a9Emm32bf8BvMM/edit?usp=sharing)를 통해 웹에서도 확인이 가능합니다.