package com.project.clothingaggregator.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.project.clothingaggregator..*.*(..))")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        logger.info("Вызов метода: {}", methodName);
    }

    @AfterReturning(pointcut = "execution(* com.project.clothingaggregator..*.*(..))", returning = "result")
    public void logMethodSuccess(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().toShortString();
        logger.info("Метод {} выполнен успешно. Результат: {}", methodName, result);
    }

    @AfterThrowing(pointcut = "execution(* com.project.clothingaggregator..*.*(..))", throwing = "ex")
    public void logMethodError(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().toShortString();
        logger.error("Ошибка в методе {}: {}", methodName, ex.getMessage());
    }
}
