package kz.iitu.lab3.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Order(2)
public class LoggingAspect {

    @Before("kz.iitu.lab3.aspect.Pointcuts.serviceOperation()")
    public void before(JoinPoint jp) {
        System.out.println(
                "[LOG] -> " +
                        jp.getSignature().toShortString() +
                        " args=" +
                        Arrays.toString(jp.getArgs())
        );
    }

    @AfterReturning(
            pointcut = "kz.iitu.lab3.aspect.Pointcuts.serviceOperation()",
            returning = "result"
    )
    public void afterReturning(JoinPoint jp, Object result) {
        System.out.println(
                "[LOG] <- " +
                        jp.getSignature().toShortString() +
                        " returned=" +
                        result
        );
    }

    @AfterThrowing(
            pointcut = "kz.iitu.lab3.aspect.Pointcuts.serviceOperation()",
            throwing = "ex"
    )
    public void afterThrowing(JoinPoint jp, Throwable ex) {
        System.out.println(
                "[LOG] !! " +
                        jp.getSignature().toShortString() +
                        " threw " +
                        ex.getClass().getSimpleName() +
                        ": " +
                        ex.getMessage()
        );
    }
}