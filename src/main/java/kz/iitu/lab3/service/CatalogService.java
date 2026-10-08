
package kz.iitu.lab3.service;

// Құпия параметрлерді белгілеу үшін
import kz.iitu.lab3.audit.Sensitive;

// Audit операцияларын белгілеу үшін
import kz.iitu.lab3.audit.Audited;

// Spring annotation-дары
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

// Java құралдары
import java.util.List;
import java.util.stream.IntStream;

// Бұл класс бизнес-логиканы орындайды
@Service
public class CatalogService {

    // Spring proxy арқылы өз методтарымызды шақыру үшін
    @Autowired
    @Lazy
    private CatalogService self;

    // ID арқылы бір тауарды іздейміз
    public String findById(long id) {

        // Метод 50 миллисекунд күтеді
        sleep(50);

        // Тауардың атауын қайтарады
        return "Item no. " + id;
    }

    // Тауарлар тізімін алу операциясын тіркейміз
    @Audited(
            action = "CATALOG_LIST",
            logArguments = true
    )
    public List<String> findAll(int limit) {

        // 300 миллисекунд күтеді
        sleep(300);

        // 1-ден limit-ке дейін тауарлар тізімін жасайды
        return IntStream.rangeClosed(1, limit)
                .mapToObj(i -> "Item no. " + i)
                .toList();
    }

    // Тауарды өшіру операциясын тіркейміз
    @Audited(action = "CATALOG_REMOVE")
    public String remove(long id) {

        // ID дұрыс болмаса, қате шығарамыз
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid identifier: " + id
            );
        }

        // Тауар өшірілгені туралы жауап қайтарамыз
        return "Removed item no. " + id;
    }

    // Self-invocation мәселесін шешетін метод
    public String removeTwice(long id) {

        // Proxy арқылы бірінші тауарды өшіреміз
        String first = self.remove(id);

        // Proxy арқылы екінші тауарды өшіреміз
        String second = self.remove(id + 1);

        // Екі операцияның нәтижесін қайтарамыз
        return first + "; " + second;
    }

    // ЖАҢА МЕТОД: Argument Masking тексеру
    public String login(
            String username,
            @Sensitive String password
    ) {

        // username — ашық ақпарат
        // password — құпия ақпарат

        // Бұл мысалда нақты авторизация жасалмайды
        // Тек AOP механизмін тексереміз

        return "Login request received for " + username;
    }

    // Методты уақытша кідіртуге арналған
    private void sleep(long ms) {

        try {
            Thread.sleep(ms);

        } catch (InterruptedException e) {

            // Ағынның interrupted күйін сақтаймыз
            Thread.currentThread().interrupt();
        }
    }
}
