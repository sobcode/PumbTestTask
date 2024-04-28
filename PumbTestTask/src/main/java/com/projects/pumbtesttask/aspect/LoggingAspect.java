package com.projects.pumbtesttask.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.projects.pumbtesttask..*.*(..))  " +
            "&& !execution(* com.projects.pumbtesttask..*.*" +
            "(@org.springframework.web.bind.annotation.ExceptionHandler (*), ..))")
    public Object aroundEveryMethod(ProceedingJoinPoint pjp) throws Throwable {
        String className = pjp.getTarget().getClass().getSimpleName();
        String methodName = pjp.getSignature().getName();

        log.info(String.format("LOG: %s.%s start", className, methodName));

        Object object = pjp.proceed();

        log.info(String.format("LOG: %s.%s end", className, methodName));

        return object;
    }
}
