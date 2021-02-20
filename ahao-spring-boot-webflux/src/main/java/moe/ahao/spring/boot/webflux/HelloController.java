package moe.ahao.spring.boot.webflux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, WebFlux !";
    }

    /**
     * @return Mono 返回 0 或 1 个元素, 即单个对象
     */
    @GetMapping("/mono")
    public Mono<String> mono() {
        return Mono.create(s -> s.success("success"));       // 使用 MonoSink 来创建 Mono
        // return Mono.justOrEmpty(Optional.ofNullable(null));  // 从一个 Optional 对象或 null 对象中创建 Mono。
        // return Mono.error(new Exception("error"));           // 创建一个只包含错误消息的 Mono
        // return Mono.never();                                 // 创建一个不包含任何消息通知的 Mono
        // return Mono.delay(Duration.ofSeconds(10));           // 在指定的延迟时间之后，创建一个 Mono，产生数字 0 作为唯一值
    }

    /**
     * @return Flux 要么成功发布 0 到 N 个元素，要么错误
     */
    @GetMapping("/flux")
    public Flux<String> flux() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("item" + i);
        }
        return Flux.fromIterable(list); // Mono 返回 0 或 1 个元素, 即单个对象
    }
}
