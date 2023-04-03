package hello.typeconverter.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;

// "127.0.0.1:8080"이라는 문자가 들어오는 경우, ip 문자와 포트 각각을 생성하는 방법입니다.
@Getter
@EqualsAndHashCode
public class IpPort {

    private String ip;
    private int port;

    public IpPort(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
