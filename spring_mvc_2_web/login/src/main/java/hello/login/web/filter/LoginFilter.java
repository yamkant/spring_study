package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

// TODO: logback mdc 검색해보기 -> 한 유저가 애플리케이션에서 로그를 찍는 경우, 단일 요청에 대해 어떤 요청인지 추적할 수 있도록 해줍니다.
//       error / info 상황에서도 request 객체의 정보를 남길 수 있게 됩니다.
@Slf4j
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();
        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            // NOTE: 다음 필터가 있다면, 다음 필터가 호출되고 없다면 다음 Servlet을 호출합니다.
            //       해당 구문이 없으면 로그를 찍고 동작이 멈춰버리기 때문에, 필수적으로 사용해야합니다.
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
