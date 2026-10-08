
package kz.iitu.lab3.aspect;

// Құпия параметрлерді белгілеу үшін
import kz.iitu.lab3.audit.Sensitive;

// AOP annotation және метод ақпараттары
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.reflect.MethodSignature;

// Spring annotation
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// Java құралдары
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect // Бұл класс AOP аспект екенін көрсетеді
@Component // Spring объектіні автоматты түрде жасайды
@Order(2) // Аспектінің орындалу басымдығы
public class LoggingAspect {

    // Метод басталмай тұрып жұмыс істейді
    @Before("kz.iitu.lab3.aspect.Pointcuts.serviceOperation()")
    public void before(JoinPoint jp) {

        // Шақырылған методтың ақпаратын аламыз
        MethodSignature signature =
                (MethodSignature) jp.getSignature();

        // Методтың өзін аламыз
        Method method = signature.getMethod();

        // Методқа берілген аргументтерді аламыз
        Object[] args = jp.getArgs();

        // Аргументтердің көшірмесін жасаймыз
        // Нақты аргументтер өзгермейді
        Object[] safeArgs = Arrays.copyOf(args, args.length);

        // Әр параметрдің annotation-дарын аламыз
        Annotation[][] annotations =
                method.getParameterAnnotations();

        // Барлық параметрлерді тексереміз
        for (int i = 0; i < annotations.length; i++) {

            // Әр параметрдің annotation-дарын қараймыз
            for (Annotation annotation : annotations[i]) {

                // Егер @Sensitive болса, мәнін жасырамыз
                if (annotation instanceof Sensitive) {
                    safeArgs[i] = "***";
                }
            }
        }

        // Метод атауын және қауіпсіз аргументтерді шығарамыз
        System.out.println(
                "[LOG] -> " +
                        jp.getSignature().toShortString() +
                        " args=" +
                        Arrays.toString(safeArgs)
        );
    }

    // Метод сәтті аяқталғанда жұмыс істейді
    @AfterReturning(
            pointcut = "kz.iitu.lab3.aspect.Pointcuts.serviceOperation()",
            returning = "result"
    )
    public void afterReturning(JoinPoint jp, Object result) {

        // Метод атауын және қайтарған нәтижесін шығарамыз
        System.out.println(
                "[LOG] <- " +
                        jp.getSignature().toShortString() +
                        " returned=" +
                        result
        );
    }

    // Метод қате шығарғанда жұмыс істейді
    @AfterThrowing(
            pointcut = "kz.iitu.lab3.aspect.Pointcuts.serviceOperation()",
            throwing = "ex"
    )
    public void afterThrowing(JoinPoint jp, Throwable ex) {

        // Қатенің түрі мен хабарламасын шығарамыз
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
