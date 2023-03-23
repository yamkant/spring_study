package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

// NOTE: 쿠키에 만료시간을 설정하지 않으면, 세션 저장소에 세션이 무한정으로 남아있게 됩니다.
//       이 때, 메모리의 크기가 무한하지 않기 때문에 이를 잘 관리해야합니다.
@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없습니다.";
        }

        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name)));

        log.info("sessionId={}", session.getId());
        // NOTE: 만료 전까지 브라우저에서의 작동을 하는 경우에는 세션이 갱신되며, 일정 시간 이상이 되는 경우 세션이 만료됩니다.
        // NOTE: 세션의 주기를 코드로 설정하는 방법은 setMaxInactiveInterval()을 사용하는 것입니다.
        // NOTE: 글로벌로 전체 변경하는 경우, application.properties에서 처리합니다.
        log.info("getMaxInactiveInterval={}", session.getMaxInactiveInterval());
        log.info("creationTime={}", new Date(session.getCreationTime()));
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));
        log.info("isNew={}", session.isNew());

        return "세션 출력";
    }
}
