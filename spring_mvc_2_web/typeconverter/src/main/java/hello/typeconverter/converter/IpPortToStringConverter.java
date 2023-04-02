package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class IpPortToStringConverter implements Converter<IpPort, String> {
    @Override
    public String convert(IpPort source) {
        log.info("convert source={}", source);
        // IpPort 객체를 String으로 변환시키는 방법입니다.
        return source.getIp() + ":" + source.getPort();
    }
}
