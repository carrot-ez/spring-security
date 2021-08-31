# Spring Security

spring security

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
