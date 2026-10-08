
package kz.iitu.lab3.aspect;

// Құпия параметрді анықтайтын өзіміздің annotation
import kz.iitu.lab3.audit.Sensitive;

// Метод туралы ақпарат алу үшін
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

// Spring бұл класты автоматты түрде тануы үшін
import org.springframework.stereotype.Component;

// Метод параметрлерін тексеру үшін
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.aspectj.lang.reflect.MethodSignature;

// Аргументтер тізімін сақтау үшін
import java.util.Arrays;

@Aspect // Бұл класс AOP аспект екенін көрсетеді
@Component // Spring осы кластың объектісін жасайды
public class ArgumentMaskingAspect {

    // Метод орындалмай тұрып іске қосылады
    @Before("kz.iitu.lab3.aspect.Pointcuts.serviceOperation()")
    public void maskArguments(JoinPoint joinPoint) {

        // Қай метод шақырылғанын анықтаймыз
        MethodSignature signature =
                (MethodSignature) joinPoint.getSignature();

        // Шақырылған методтың өзін аламыз
        Method method = signature.getMethod();

        // Методқа берілген нақты аргументтерді аламыз
        Object[] args = joinPoint.getArgs();

        // Аргументтердің көшірмесін жасаймыз
        // Нақты аргументтер өзгермеуі керек
        Object[] maskedArgs = Arrays.copyOf(args, args.length);

        // Әр параметрдің annotation-дарын аламыз
        Annotation[][] annotations =
                method.getParameterAnnotations();

        // Барлық параметрлерді тексереміз
        for (int i = 0; i < annotations.length; i++) {

            // Әр параметрдегі annotation-дарды қараймыз
            for (Annotation annotation : annotations[i]) {

                // Егер параметр @Sensitive деп белгіленсе
                if (annotation instanceof Sensitive) {

                    // Нақты мәнді логта *** деп ауыстырамыз
                    maskedArgs[i] = "***";
                }
            }
        }

        // Қауіпсіз аргументтерді консольге шығарамыз
        System.out.println(
                "[MASK] " +
                        method.getName() +
                        " args=" +
                        Arrays.toString(maskedArgs)
        );
    }
}
