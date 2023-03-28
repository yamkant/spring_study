package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// NOTE: 이 Exception을 사용하는 경우, annotation에 있는 상태값을 가지고 applyStatusAndReason 클래스에서 이를 처리합니다.
// ResponseStatusExceptionResolver를 찾아보면 해당 어노테이션들을 어떻게 사용하는지 볼 수 있습니다.
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
public class BadRequestException extends RuntimeException {
}
