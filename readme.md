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
