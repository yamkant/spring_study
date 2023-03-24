package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

@Slf4j
@Controller
public class ServletExController {

    @GetMapping("/error-ex")
    public void errorEx() {
        // NOTE: Exception이 WAS까지 전달된다면, 서버에서 해결할 수 없는 문제가 발생하였다고 생각하고
        //       톰캣 기본 오류 페이지를 띄웁니다. (404 / 500 등)
        throw new RuntimeException("예외 발생!");
    }

    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 오류!");
    }

    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        // NOTE: 500 에러는 메시지를 함께 던지지 않습니다.
        response.sendError(500);
    }
}
