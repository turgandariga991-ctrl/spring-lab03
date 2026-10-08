// Бұл класс біздің жобаның audit пакетінде орналасқан
package kz.iitu.lab3.audit;

// Annotation қай жерге қойылатынын анықтау үшін қажет
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

// Annotation бағдарлама жұмыс істеп тұрған кезде
// қолжетімді болуы үшін қажет
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// Бұл annotation тек метод параметрлеріне қойылады
@Target(ElementType.PARAMETER)

// RUNTIME — бағдарлама жұмыс істеп тұрғанда
// annotation-ды оқуға мүмкіндік береді
@Retention(RetentionPolicy.RUNTIME)

// Өзіміз жасаған Sensitive annotation
public @interface Sensitive {

    // Іші бос, себебі бізге тек параметрдің
    // құпия екенін белгілеу жеткілікті
}