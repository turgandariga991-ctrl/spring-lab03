package kz.iitu.lab3.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(3)
public class TimingAspect {

    @Around("kz.iitu.lab3.aspect.Pointcuts.serviceOperation()")
    public Object measure(ProceedingJoinPoint pjp) throws Throwable {

        long started = System.nanoTime();

        try {
            return pjp.proceed();
        } finally {

            long ms =
                    (System.nanoTime() - started) / 1_000_000;

            String name =
                    pjp.getSignature().toShortString();

            if (ms > 200) {
                System.out.println(
                        "[TIME] SLOW: " +
                                name + " — " + ms + " ms"
                );
            } else {
                System.out.println(
                        "[TIME] " +
                                name + " — " + ms + " ms"
                );
            }
        }
    }
}