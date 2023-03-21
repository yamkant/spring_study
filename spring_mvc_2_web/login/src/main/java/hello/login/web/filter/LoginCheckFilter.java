package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    // NOTE: 로그인하지 않더라도 들어올 수 있는 페이지 리스트를 작성합니다.
    private static final String[] whiteList = {"/", "/members/add", "/login", "/css/*"};

    // NOTE: implements 하더라도, default 키워드가 붙으면 굳이 구현하지 않아도 됩니다.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try{
            log.info("인증 체크 필터 시작 {}", requestURI);

            if (isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청 {}", requestURI);
                    // 로그인 페이지로 리다이렉션 수행 -> 로그인 이후, 다시 현재 페이지로 돌아올 수 있도록 설정해둡니다.
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    // 다음 서블릿이나 컨트롤러를 호출하지 않습니다.
                    return;
                }
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e; // 예외도 로깅이 가능하지만, 톰캣까지 예외를 보내줘야합니다.
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }
    }
    /**
     * 화이트 리스트의 경우 인증 체크 X
     */
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }
}
