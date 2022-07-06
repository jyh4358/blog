# JDong's Blog

나만의 블로그 웹 사이트 개발 프로젝트

<br/>
<br/>


## 목차

<br/>

1. [개발 환경](#개발-환경)
2. 사용 기술
3. 블로그 프로젝트를 기획한 이유
4. 패키지 구조
5. E-R 다이어그램
6. 핵심 기능
    - 소셜 로그인
    - 로그 추적기
    - Toast Ui editor
    - 댓글 기능
    - 카테고리 편집 기능
    - 게시물 자동 저장 기능
    - 게시물 깃헙 백업 기능
    - 공유하기 기능
    - 키워드, 태그 검색 기능
7. 프로젝트를 통해 느낀점



## 1. 개발 환경

- IntelliJ
- GitHub
- Mysql Workbench 8.0
- Postman

## 2. 사용 기술

### Back-End

- Java : Java 11 openJDK
- Spring 
  - SpringBoot 2.6.6
  - Spring Security
  - Spring Data JPA
  - QueryDSL

### Front-End

- Html / css
- Javascript
- JQuery
- BootStrap 5

### Infra

- AWS EC2
- AWS S3
- Jenkins

### 기타 라이브러리
- Lombok
- Github-api
- Toast UI Editor
- Slick-Carousel


## 3. 블로그 프로젝트를 기획한 이유

준비된 웹 개발자가 되기위해 무엇을 준비해야할까? 바로 끊임없는 열정과 노력.

지금까지 공부 및 개발 기록을 티스토리 블로그에 작성하여 관리했지만 **내 입맛에 맞춘 블로그를 직접 만들어 사용하면 어떨까?**라는 
생각을 갖게 되었습니다. 개발 공부를 통한 저의 성장을 블로그에 남김과 동시에 그 성장으로 인한 지속적인 블로그 개발 및 유지보수를 통하여 같이 
성장할 수 있는 **개발자의 동반자**를 만들기로 결정했습니다.


## 4. 도메인형 패키지 구조

계층형 패키지 구조는 상단 디렉터리로 빠르게 파악할 수 있지만 장점이 있지만
저는 관련 코드가 응집되어 이후에 유지보수가 더 용이할 것같아 도메인형 패키지 구조를 선택하였습니다.


![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%ED%8C%A8%ED%82%A4%EC%A7%80+%EA%B5%AC%EC%A1%B0.JPG)


## 5. E-R 다이어그램


![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/er.JPG)


## 6. 핵심 기능
    - 도메인 주도 설계 ㅌ
    - 소셜 로그인 ㅌ
    - 로그 추적기 ㅌ
    - Toast Ui editor ㅌ
    - 댓글 기능 ㅌ
    - 카테고리 편집 기능  ㅌ
    - 게시물 자동 저장 기능 ㅌ
    - 게시물 깃헙 백업 기능 ㅌ
    - 공유하기 기능
    - 키워드, 태그 검색 기능
    - rest api

### 소셜 로그인

Spring Security와 OAuth2 인증방식을 통해 소셜 로그인을 구현하였으며 현재는 구글 소셜 로그인만 가능하지만 이후에 소셜 provider에 따른 
추가 확장 할 수 있도록 OAuth2UserInfo 추상 클래스를 이용하여 구현하였습니다.

![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%EB%A1%9C%EA%B7%B8%EC%9D%B8gif2.gif)

### 로그 추적기

Spring AOP를 이용하여 Controller, Service, Repository에 포인트컷을 지정하여 로그 추적기 기능을 구현하였으며 또한
요청별로 로그를 추적할 수 있도록 쓰레드 로컬을 이용하였습니다.


![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%EB%A1%9C%EA%B7%B8.JPG)



해당 기능 개발을 위해 인프런, 김영한님의 스프링 핵심 원리 - 고급편을 참고하였습니다.

<br/>
<br/>

### 게시물 깃헙 백업 기능

Github api를 이용하여 게시물 작성 시 자동으로 Github repository에 게시글이 push 되도록 구현하였습니다.
![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%EA%B9%83%ED%97%99+%EB%B0%B1%EC%97%85.gif)

<br/>
<br/>

### TOAST UI Editor

블로그 글을 게시할 때 확장성이 좋고 소스 코드 입력에 수월한 마크다운 에디터를 사용하기 위해 국내에서 개발한 TOAST UI Editor를
사용하였습니다. TOAST UI Editor는 기본적으로 컨텐츠 내에서 이미지 삽입 시 blob로 컨텐츠에 포함되기 때문에 DB에 부담이 되지 않도록 
컨텐츠에 이미지 삽입 시 해당 이미지 파일은 AWS S3에 저장하여 반환된 이미지 경로 url를 컨텐츠에 포함시켰습니다.
![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%EA%B8%80%EC%9E%91%EC%84%B1.JPG)

### 이미지 저장 - AWS S3

썸네일 이미지와 게시글 작성시 첨부되는 이미지들은 AWS S3에 저장하였습니다.


### 작성중인 게시물 자동 저장 기능

글작성 도중 5분마다 글을 저장하는 기능을 구현했습니다.

### 카테고리 편집 기능 

카테고리 엔티티는 셀프조인으로 자식 카테고리가 부모 카테고리를 참조할 수 있도록 구현하였습니다. 또한 추가, 삭제, 수정 기능을 가진
api를 구현하여 백단에서는 변경된 카테고리 리스트와 기존 카테고리 리스트 두개를 비교하여 신규 카테고리 생성, 기존 카테고리명 변경, 
기존 카테고리 삭제를 할 수 있도록 구현하였습니다.

### 댓글 기능

댓글 기능 또한 카테고리와 마찬가지로 셀프조인으로 대댓글 작성시 부모 카테고리를 참조할 수 있도록 구현하였으며
api로 댓글 조회, 댓글 작성, 댓글 삭제를 api로 구현하였습니다.

### 게시글 공유하기 기능


