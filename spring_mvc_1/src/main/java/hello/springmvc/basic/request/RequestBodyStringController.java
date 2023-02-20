package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        response.getWriter().write("ok");
    }

    // NOTE: 요청의 body만 필요하기 때문에, servlet request 객체를 모두 가지고 있는 방식이 아닌, 필요한 부분의 값만 가질 수 있습니다.
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        responseWriter.write("ok");
    }

    // NOTE: HTTP message를 스펙화하여 각 자료형에 맞게 가져오는 방식입니다. http message를 그냥 주고 받는 방식으로 사용합니다.
    // NOTE: HttpEntity: 요청파라미터(쿼리파라미터)와 별개로 getHeader(), getBody()를 통해 데이터를 직접 조회하는 방식으로 사용합니다.
    // NOTE: view를 조회하지 않고, HTTP body 응답에 바로 넣어서 반환합니다.
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {

        String messageBody = httpEntity.getBody();
        log.info("messageBody={}", messageBody);

        return new HttpEntity<String>("ok");
    }

    // NOTE: 요청은 @RequestBody, 응답은 @ResponseBody에서 처리합니다.
    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) {
        // Header 정보를 받기위해, 위에서 사용했던 HttpEntity, @RequestHeader를 사용하면 됩니다.
        log.info("messageBody={}", messageBody);

        return "ok";
    }

    // 정리: 요청 파라미터를 조회하기 위해서는 @RequestParam, @ModelAttribute를 사용하고, (RequestParamController.java)
    //      HTTP 메시지 바디를 직접 조회하기 위해서는 @RequestBody를 사용합니다. (RequestBodyStringController.java)
}
