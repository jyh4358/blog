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
    - rest api
    - 에러 처리
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

- Java 11 openJDK
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

지금까지 공부 및 개발 기록을 티스토리 블로그에 작성하여 관리했지만 **내 입맛에 맞춘 블로그를 직접 만들어 사용하면 어떨까?** 라는 
생각을 갖게 되었습니다. 개발 공부를 통한 저의 성장을 블로그에 남김과 동시에 그 성장으로 인한 지속적인 블로그 개발 및 유지보수를 통하여 같이 
성장할 수 있는 **개발자의 동반자**를 만들기로 결정했습니다.


## 4. 도메인형 패키지 구조

계층형 패키지 구조는 상단 디렉터리로 빠르게 파악할 수 있지만 장점이 있지만
저는 관련 코드가 응집되어 이후에 유지보수가 더 용이한 도메인형 패키지 구조를 선택하였습니다.


![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%ED%8C%A8%ED%82%A4%EC%A7%80+%EA%B5%AC%EC%A1%B0.JPG)


## 5. E-R 다이어그램


![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/blog-er.JPG)


## 6. 핵심 기능
- 소셜 로그인
- 로그 추적기
- api
- 게시물 깃헙 백업 기능
- Toast Ui editor
- tagify를 이용한 태그 기능 구현
- 댓글 기능
- QueryDSL의 동적 쿼리를 이용한 검색 기능
- 게시물 자동 저장 기능
- 카테고리 편집 기능
- 공유하기 기능

### 소셜 로그인

Spring Security와 OAuth2 인증방식을 통해 소셜 로그인을 구현하였으며 현재는 구글 소셜 로그인만 가능하지만 이후에 소셜 provider에 따른 
추가 확장 할 수 있도록 OAuth2UserInfo 추상 클래스를 이용하여 구현하였습니다.

[OAuth2UserInfo 추상 클래스](https://github.com/jyh4358/blog/blob/master/src/main/java/com/myblog/security/oauth2/model/OAuth2UserInfo.java)


![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%EB%A1%9C%EA%B7%B8%EC%9D%B8gif2.gif)

### 로그 추적기

Spring AOP를 이용하여 Controller, Service, Repository에 포인트컷을 지정하여 로그 추적기 기능을 구현하였으며 또한
요청별로 로그를 추적할 수 있도록 쓰레드 로컬을 이용하였습니다.

[로그 추적기 패키지](https://github.com/jyh4358/blog/tree/master/src/main/java/com/myblog/common/log)

![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%EB%A1%9C%EA%B7%B8.JPG)

해당 기능 개발을 위해 인프런, 김영한님의 스프링 핵심 원리 - 고급편을 참고하였습니다.

<br/>
<br/>

### rest api

페이지 요청을 제외한 get, post, patch, delete 요청은 rest api 방식으로 구현하여 자원의 행위를 보다 직관적으로 나타내도록 했습니다.

예시 코드
- [ArticleApiController 코드](https://github.com/jyh4358/blog/blob/master/src/main/java/com/myblog/article/controller/ArticleApiController.java)
- [article.js ajax 요청](https://github.com/jyh4358/blog/blob/master/src/main/resources/static/js/article.js)


<br/>
<br/>

### 에러 처리

스프링 부트에서 제공하는 BasicErrorController를 이용하여 Html 오류 페이지를 처리하였고 
api 관련 예외는 RestControllerAdvice를 이용하여 예외 처리 메서드들을 관리하였습니다.

예외에 따른 메시지와 코드를 관리하기 쉽도록 enum 클래스를 만들어 이후에 유지보수가 용이하도록 구현하였습니다.

[ExceptionMessage](https://github.com/jyh4358/blog/blob/master/src/main/java/com/myblog/common/exception/ExceptionMessage.java)


<br/>
<br/>

### 게시물 깃헙 백업 기능

Github api를 이용하여 게시물 작성 시 자동으로 Github repository에 게시글이 push 되도록 구현하였습니다.

![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%EA%B9%83%ED%97%99+%EB%B0%B1%EC%97%85%EC%88%98%EC%A0%95.gif)

<br/>
<br/>

### TOAST UI Editor

블로그 글을 게시할 때 확장성이 좋고 소스 코드 입력에 수월한 마크다운 에디터를 사용하기 위해 국내에서 개발한 TOAST UI Editor를
사용하였습니다. TOAST UI Editor는 기본적으로 컨텐츠 내에서 이미지 삽입 시 blob로 컨텐츠에 포함되기 때문에 DB에 부담이 되지 않도록 
컨텐츠에 이미지 삽입 시 해당 이미지 파일은 AWS S3에 저장하여 반환된 이미지 경로 url를 컨텐츠에 포함시켰습니다.

![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%EA%B8%80%EC%9E%91%EC%84%B1.JPG)

<br/>
<br/>

### 이미지 저장 - AWS S3

썸네일 이미지와 게시글 작성시 첨부되는 이미지들은 첨부되는 순간 비동기 통신으로 AWS S3에 저장한 뒤 이미지 url을 가져오는 방식을
사용했습니다.

![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%EC%9D%B4%EB%AF%B8%EC%A7%80+%EC%82%BD%EC%9E%85.gif)


<br/>
<br/>

### QueryDSL의 동적 쿼리를 이용한 검색 기능

검색 관련 부분은 QueryDSL의 동적 쿼리 메서드를 이용하여 구현하였습니다.

특히 카테고리 경우 부모 카테고리로 검색할때는 하위 자식 카테고리에 포함된 모든 게시물들을 검색해야 했기 때문에 부모 카테고리 검색과,
자식 카테고리 검색의 경우 분기처리를 해줘야 했습니다. 하지만 QueryDSL을 이용하여 분기처리 없이 동적 쿼리 메서드를 이용하여 검색이 가능하도록
구현하였습니다.

[QeuryDSL을 적용한 repository](https://github.com/jyh4358/blog/blob/master/src/main/java/com/myblog/article/repository/ArticleSearchRepository.java)

![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%EA%B2%80%EC%83%89.gif)

<br/>
<br/>


### 작성중인 게시물 자동 저장 기능

글작성 도중 5분마다 글을 저장하여 새로 글작성 시 저장한 글을 가져오도록 구현하였습니다.

[자동 저장 ajax 요청 코드](https://github.com/jyh4358/blog/blob/master/src/main/resources/static/js/autoSave.js)

![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%EC%9E%90%EB%8F%99%EC%A0%80%EC%9E%A5.gif)


<br/>
<br/>

### 카테고리 편집 기능 

카테고리 엔티티는 셀프조인으로 자식 카테고리가 부모 카테고리를 참조할 수 있도록 구현하였습니다.
추가, 삭제, 수정 기능을 가진 api를 구현하여 백단에서는 변경된 카테고리 리스트와 기존 카테고리 리스트 두개를 비교하여 신규 카테고리 생성, 
기존 카테고리명 변경, 기존 카테고리 삭제를 할 수 있도록 구현하였습니다.

[백단 category 코드](https://github.com/jyh4358/blog/blob/master/src/main/java/com/myblog/category/service/CategoryService.java)

![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%EC%B9%B4%ED%85%8C%EA%B3%A0%EB%A6%AC.gif)

<br/>
<br/>


### tagify를 이용한 태그 기능 구현

tagify 라이브러리를 이용하여 태그 기능을 이용하여 게시물 작성시 기존의 등록한 태그를 사용하거나 새로 태그를 추가할 수 있도록 구현했습니다.

![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%ED%83%9C%EA%B7%B8%ED%8C%8C%EC%9D%B4.gif)

<br/>
<br/>

### 댓글 기능

댓글 기능 또한 카테고리와 마찬가지로 셀프조인으로 대댓글 작성시 부모 카테고리를 참조할 수 있도록 구현하였으며
댓글 조회, 댓글 작성, 댓글 삭제를 api로 비동기 처리 방식으로 구현하였습니다.

또한 댓글에 비밀 기능을 추가하여 본인과 관리자만 비밀 댓글을 볼 수 있도록 하였습니다.

[ajax 요청 코드](https://github.com/jyh4358/blog/blob/master/src/main/resources/static/js/comment.js)

![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%EB%8C%93%EA%B8%80.gif)

<br/>
<br/>

### 게시글 공유하기 기능

블로그의 접근성을 높이기 위해 카카오톡, 네이버 블로그, 페이스북 공유 기능을 추가하였습니다.

**카카오 공유 예시**

![image](https://file-upload-store-jdd.s3.ap-northeast-2.amazonaws.com/%EC%B9%B4%EC%B9%B4%EC%98%A4+%EA%B3%B5%EC%9C%A0.JPG)


<br/>
<br/>


## 프로젝트를 마무리하며

공부를 시작 한지 이제 6개월이 지나면서 이번 1인 프로젝트를 완성하여 아직 부족한 점도 많이 느낌과 동시에 많이 성장하고 있구나 라고
느끼게 되었습니다. 코드를 구현하면서 구현한 코드를 보며 성취감를 맛보고, 더 좋은 구현 방법을 보며 코드를 리펙토링하여 성장함을 맛보고, 
또한 문제를 직면했을 때 해당 문제를 해결하기 위해 머리를 싸매며 몰두하는 제 자신을 발견했습니다.

잠깐 휴식을 취할때나, 밥먹을 때도 직면한 문제에 대해 생각하고, 고민하면서 좋은 방법이 생각나면 다시 돌아가서 코딩하여 
해결하는 저의 모습을 보면서 어떤 문제가 닥치더라도 해결할 수 있는 자신감을 갖게 되었습니다.

이번 프로젝트 경험을 밑바탕으로 아직은 공부하며 배워가야할 부분들이 많지만 꾸준한 공부를 통하여 **현재의 '나'보다 내일의 '내가' 
더 성장한 개발자**가 되는 제 모습을 그리게 되는 값진 경험을 갖게 되었습니다.




