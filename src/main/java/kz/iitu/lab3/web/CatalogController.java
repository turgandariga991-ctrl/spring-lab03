
package kz.iitu.lab3.web;

// Бизнес-логика орналасқан сервис
import kz.iitu.lab3.service.CatalogService;

// Spring AOP proxy-ді тексеру үшін
import org.springframework.aop.support.AopUtils;

// REST API жасау үшін
import org.springframework.web.bind.annotation.*;

// Тізім және Map қолдану үшін
import java.util.List;
import java.util.Map;

// Бұл класс HTTP сұраныстарды қабылдайды
@RestController

// Барлық endpoint-тің ортақ адресі
@RequestMapping("/api/lab4")
public class CatalogController {

    // Сервис объектісін сақтаймыз
    private final CatalogService catalogService;

    // Spring сервис объектісін автоматты түрде береді
    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    // ID арқылы бір тауарды алу
    // Мысалы: GET /api/lab4/item/5
    @GetMapping("/item/{id}")
    public String findById(@PathVariable long id) {

        // Сервистегі findById методын шақырамыз
        return catalogService.findById(id);
    }

    // Барлық тауарларды көрсету
    // Мысалы: GET /api/lab4/items?limit=5
    @GetMapping("/items")
    public List<String> findAll(
            @RequestParam(defaultValue = "5") int limit) {

        // Тауарлар тізімін қайтарамыз
        return catalogService.findAll(limit);
    }

    // ID арқылы тауарды өшіру
    // Мысалы: DELETE /api/lab4/item/5
    @DeleteMapping("/item/{id}")
    public String remove(@PathVariable long id) {

        // Сервистегі remove методын шақырамыз
        return catalogService.remove(id);
    }

    // Spring Proxy туралы ақпарат алу
    // Мысалы: GET /api/lab4/proxy
    @GetMapping("/proxy")
    public Map<String, String> proxyInfo() {

        // Proxy туралы мәліметтерді қайтарамыз
        return Map.of(

                // Spring жасаған объектінің толық атауы
                "className",
                catalogService.getClass().getName(),

                // Негізгі класстың атауы
                "superClass",
                catalogService.getClass()
                        .getSuperclass()
                        .getSimpleName(),

                // Бұл объект AOP proxy ме?
                "isAopProxy",
                String.valueOf(
                        AopUtils.isAopProxy(catalogService)
                ),

                // Бұл CGLIB proxy ме?
                "isCglib",
                String.valueOf(
                        AopUtils.isCglibProxy(catalogService)
                )
        );
    }

    // Self-invocation тексеру
    // Мысалы: GET /api/lab4/remove-twice/5
    @GetMapping("/remove-twice/{id}")
    public String removeTwice(@PathVariable long id) {

        // Екі remove операциясын орындаймыз
        return catalogService.removeTwice(id);
    }

    // ЖАҢА ENDPOINT: Argument Masking тексеру
    // POST қолданамыз, себебі пароль URL ішінде болмауы керек
    @PostMapping("/login")
    public String login(
            @RequestBody LoginRequest request
    ) {

        // Username мен password-ты сервиске жібереміз
        // @Sensitive белгісі сервис методында орналасқан
        return catalogService.login(
                request.username(),
                request.password()
        );
    }

    // JSON ішіндегі username және password мәндерін қабылдайды
    // Бұл тек лабораториялық тестке арналған
    public record LoginRequest(
            String username,
            String password
    ) {
    }
}
