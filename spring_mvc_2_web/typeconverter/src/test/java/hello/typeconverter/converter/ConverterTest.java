package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConverterTest {

    @Test
    void stringToInteger() {
        StringToIntegerConverter converter = new StringToIntegerConverter();
        Integer result = converter.convert("10");
        assertThat(result).isEqualTo(10);
    }

    @Test
    void IntegerToString() {
        IntegerToStringConverter converter = new IntegerToStringConverter();
        String result = converter.convert(10);
        assertThat(result).isEqualTo("10");
    }

    @Test
    void stringToIpPort() {
        IpPortToStringConverter converter = new IpPortToStringConverter();
        IpPort source = new IpPort("127.0.0.1", 8080);
        String result = converter.convert(source);
        assertThat(result).isEqualTo("127.0.0.1:8080");
    }

    @Test
    void ipPortToString() {
        StringToIpPortConverter converter = new StringToIpPortConverter();
        String source = "127.0.0.1:8080";
        IpPort result = converter.convert(source);
        // NOTE: IpPort의 @EqualsAndHashCode 애노테이션을 사용하면, 내부 ip와 port 값이 같으면
        //       객체가 아니더라도, 동일하게 취급됩니다.
        assertThat(result).isEqualTo(new IpPort("127.0.0.1", 8080));

    }
}
