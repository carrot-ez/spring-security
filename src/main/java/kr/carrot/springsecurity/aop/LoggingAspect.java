package kr.carrot.springsecurity.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    /**
     * 1. controller 하위 패키지의 모든 메소드
     * 2. service 하위 패키지의 모든 메소드
     * 두 경우의 메소드 실행 전/후를 로깅하는 AOP Advice
     * > 수정시 Pointcut expression 검색하여 참고
     */
    @Around("execution(* kr.carrot.springsecurity..controller..*(..)) ||" +
            "execution(* kr.carrot.springsecurity..service..*(..))")
    public Object loggingMethods(ProceedingJoinPoint pjp) throws Throwable {

        Signature signature = pjp.getSignature();
        String className = signature.getDeclaringType().getSimpleName(); // 불필요한 package name을 제외한 class name
        String methodName = signature.getName();
        String simplePath = className + "." + methodName;

        log.debug(">>>>>>>>>>>> " + "[" + simplePath + "] START >>>>>>>>>>>> ");

        Object proceed = pjp.proceed(); // 메소드 실제 수행

        log.debug("<<<<<<<<<<<< " + "[" + simplePath + "] END   <<<<<<<<<<<< ");

        return proceed;
    }
}
