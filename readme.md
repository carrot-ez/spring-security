# Spring Security

spring security project

## 신규 토큰발급 가이드

1. [클라이언트 등록](#클라이언트-등록)
2. [로그인](#로그인)
   - 출력값인 code를 토큰발급시 사용
3. [토큰발급](#토큰발급)
   - grant_type="authorization_code" 로 요청 

## APIs

### 클라이언트 등록

요청

| 항목 | 값                  |
| ---- | ------------------- |
| URL  | POST /api/client/v1 |

요청항목

| 이름        | 타입   | 필수 | 설명                     |
| ----------- | ------ | ---- | ------------------------ |
| clientId    | string | O    | Client ID                |
| redirectUri | string | O    | 요청 성공시 redirect URI |

응답예시

```json
{
    "statusCode": 201,
    "data": {
        "clientId": "1232",
        "clientSecret": "56f829c9-4437-4da5-ae9d-0ed025f64622",
        "redirectUri": "http://localhost:8080/"
    },
    "error": null
}
```

### 로그인

요청

| 항목 | 값                       |
| ---- | ------------------------ |
| URL  | POST /api/oauth/v1/login |

요청항목

| 이름     | 타입   | 필수 | 설명      |
| -------- | ------ | ---- | --------- |
| username | string | O    | 사용자ID  |
| password | string | O    | 비밀번호  |
| clientId | string | O    | Client ID |

응답 예시

```json
{
    "statusCode": 200,
    "data": "60932928-8e5d-4604-85e4-247b71a7350d",
    "error": null
}
```

### 토큰발급

요청

| 항목 | 값                      |
| ---- | ----------------------- |
| URL  | GET /api/oauth/v1/token |

요청

| 이름         | 타입   | 필수 | 설명                                   |
| ------------ | ------ | ---- | -------------------------------------- |
| grantType    | string | O    | 요청방식                               |
| clientId     | string | O    | client ID                              |
| clientSecret | string | O    |                                        |
| redirectUri  | string | O    |                                        |
| code         | string | X    | grant_type="authorization_code"인 경우 |
| refreshToken | string | X    | grant_type="refresh_token"인 경우      |

응답예시

```json
{
    "statusCode": 200,
    "data": {
        "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MzU4NDA5NDgsImV4cCI6MTYzNTg0Mjc0OCwiU0VTU0lPTl9JRCI6ImJhZWFlZTUxLTQ0Y2YtNGY4Zi1iNGM0LTcxZDcyZGJiZWQ1ZiJ9.MCk9w2aLwgGxzOixIahG0eWeXonF6HdBwcGuufz3_2M",
        "refreshToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MzU4NDA5NDgsImV4cCI6MTYzNTg1MTc0OCwiU0VTU0lPTl9JRCI6ImJhZWFlZTUxLTQ0Y2YtNGY4Zi1iNGM0LTcxZDcyZGJiZWQ1ZiJ9.yyBX5b_E4ukBWHMFCZGEclmyL_MAgZObwTikml4ndTQ",
        "tokenType": null,
        "expiresIn": null
    },
    "error": null
}
```



## logback

`logback-spring.xml` 에서 logback 설정

### default operator

`:-`

- `${LOG_FILE_MAX_SIZE :- 10MB}`
  - `LOG_FILE_MAX_SIZE` 가 존재한다면 이것 사용
  - 존재하지 않는다면 `10MB` 사용



## AOP

### dependency

```build.gradle
implementation 'org.springframework.boot:spring-boot-starter-aop'
```

#### `@EnableAspectJAutoProxy` 어노테이션

```java
@EnableAspectJAutoProxy
@SpringBootApplication
public class SpringSecurityApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}
}
```

#### `@Aspect` 컴포넌트

```java
@Aspect
@Component
public class LoggingAspect { }
```

#### `@Around`

```java
/**
     * 1. controller 하위 패키지의 모든 메소드
     * 2. service 하위 패키지의 모든 메소드
     * 두 경우의 메소드 실행 전/후를 로깅하는 AOP Advice
     */
@Around("execution(* kr.carrot.springsecurity..controller..*(..)) ||" +
        "execution(* kr.carrot.springsecurity..service..*(..))")
public Object loggingMethods(ProceedingJoinPoint pjp) throws Throwable {
    log.debug(">>>>>>>>>>>> START >>>>>>>>>>>> ");

    Object proceed = pjp.proceed(); // 메소드 실제 수행

    log.debug("<<<<<<<<<<<< END   <<<<<<<<<<<< ");

    return proceed;
}
```

> Advice 발생 시점을 정의하기 위한 Pointcut Expressiong 참고

## JWT

### dependency

```build.gradle
implementation 'io.jsonwebtoken:jjwt-api:0.11.2'
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.2',
// Uncomment the next line if you want to use RSASSA-PSS (PS256, PS384, PS512) algorithms:
//'org.bouncycastle:bcprov-jdk15on:1.60',
'io.jsonwebtoken:jjwt-jackson:0.11.2' // or 'io.jsonwebtoken:jjwt-gson:0.11.2' for gson
```

## Error

`HttpMediaTypeNotAcceptableException`

`406 Not Acceptable`

- Response를 위한 Dto 클래스에 `@Getter` 가 없어서 발생
- `CommonResponse<T>` 클래스에 `@Getter` 붙여 해결함

- Jackson은 내부적으로ObjectMapping API를 사용하여 객체를 json으로 변환하는데, Getter/Setter 가 없으면 Json으로 변환이 불가능하기 때문
