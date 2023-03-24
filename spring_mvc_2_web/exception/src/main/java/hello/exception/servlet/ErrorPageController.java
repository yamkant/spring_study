package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 예외 발생과 오류 페이지 요청 흐름: 1에서 발생한 예외에 대한 페이지를 띄우기 위에 2 수행
// 1. WAS(컨트롤러가 던진 예외처리) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
// 2. WAS 예외 발생 부분 다시 요청 -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러 -> View
// 위 두 과정을 구분할 수 있는 정보가 request.getDispatcherType입니다.
// - Request: 클라이언트 요청, ERROR: 오류 요청, 또한, FOWARD, INCLUDE, ASYNC 등등

@Slf4j
@Controller
public class ErrorPageController {

    // NOTE: 아래 내용들은 RequestDispatcher에 상수로 정의되어 있습니다.
    public static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "javax.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";



    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 404");
        printErrorInfo(request);
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 500");
        printErrorInfo(request);
        return "error-page/500";
    }

    // NOTE: error 발생시, request에 담기는 정보들을 볼 수 있습니다.
    private void printErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION: ex=", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE: {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
         // ex의 경우 NestedServletException 스프링이 한번 감싸서 반환
        log.info("ERROR_MESSAGE: {}", request.getAttribute(ERROR_MESSAGE));
        log.info("ERROR_REQUEST_URI: {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME: {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE: {}", request.getAttribute(ERROR_STATUS_CODE));

        log.info("dispatchType={}", request.getDispatcherType());
    }
}
