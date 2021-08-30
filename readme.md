# Spring Security

spring security

## logback

`logback-spring.xml` 에서 logback 설정

### default operator

`:-`

- `${LOG_FILE_MAX_SIZE :- 10MB}`
  - `LOG_FILE_MAX_SIZE` 가 존재한다면 이것 사용
  - 존재하지 않는다면 `10MB` 사용

