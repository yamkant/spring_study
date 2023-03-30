package hello.exception.exhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
// NOTE: ResponseBody가 추가된 ControllerAdvice로, RestController에서 발생한 예외를 처리합니다.
// NOTE: 어떤 컨트롤러에 대해 예외처리할 것인지 지정할 수도 있습니다.
@RestControllerAdvice(basePackages = "hello.exception.api")
public class ExControllerAdvice {

    // NOTE: 예외상태코드를 추가적으로 입력하지 않는다면, 정상처리가 되기 때문에 상태를 함께 표시해야합니다.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    // NOTE: Controller 내에 발생한 Exception을 캐치하여 JSON으로 반환합니다.
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler // NOTE: 아래 인자값 UserException과 같다면 UserException.class 생략 가능
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }

    // NOTE: 최상위 예외 클래스인 Exeption 클래스에 대한 예외
    //       -> 위에서 정의하지 않은 애들이 모두 들어옵니다. 자식 클래스의 예외가 더 자세하므로 이에 대한 예외부터 처리합니다.
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}
