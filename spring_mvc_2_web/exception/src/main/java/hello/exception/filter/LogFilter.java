package hello.exception.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    // NOTE: REQUEST 때도 logFilter가 호출되고, Error 발생시도 logFilter가 호출됩니다.
    //       DispatcherType을 통해 어떤 경우에 대한 필터 호출인지 알 수 있습니다.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        try {
            log.info("REQUEST:  [{}][{}][{}]", uuid, request.getDispatcherType(), requestURI);
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.info("EXCEPTION: {}", e.getMessage());
            // NOTE: WAS까지 올라가는 예외입니다.
            throw e;
        } finally {
            log.info("RESPONSE: [{}][{}][{}]", uuid, request.getDispatcherType(), requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}