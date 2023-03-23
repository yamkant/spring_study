package hello.login.web.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 파라미터에만 사용되는 애노테이션입니다.
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 애노테이션 정보가 남아있도록 합니다.
public @interface Login {
}
