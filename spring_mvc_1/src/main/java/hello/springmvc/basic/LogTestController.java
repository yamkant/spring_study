package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LogTestController {
    // NOTE: lombok에 내장되어있는 Slf4j에서 log 인스턴스를 자동으로 생성해줍니다.
    // private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/log-test")
    public String logTest() {
        String name = "Spring";

        System.out.println("name = " + name);

        // NOTE: log level을 /src/resources/application.properties에서 설정 가능합니다.
        // NOTE: String '+' 연산은 메서드가 호출되든 되지 않든 수행되지만, {}는 메서드 호출 시에만 사용됩니다.
        //       -> log 작성시 {}를 사용해야 의미없는 연산이 일어나지 않습니다.
        log.trace("trace log={}, {}", name);
        log.debug("debug log={}, {}", name);
        log.info("info log={}", name);
        log.warn("warn log={}", name);
        log.error("error log={}", name);

        // NOTE: 일반 @Controller의 반환값은 View Model을 위한 string 이지만,
        //       @RestController의 반환값은 html body에 값을 넣습니다.
        return "ok";
    }
}
