package hello.typeconverter;

import hello.typeconverter.converter.IntegerToStringConverter;
import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIntegerConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.formatter.MyNumberFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        // NOTE: 아래 컨버터들을 추가하지 않더라도, 스프링 내부에 구현된 컨버터들이 동작해서 잘 작동은 하지만,
        //       아래와 같이 새로 등록하는 컨버터들이 우선순위를 가집니다.
//        registry.addConverter(new StringToIntegerConverter());
//        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());

        // NOTE: 포매터를 추가하여 입력 데이터 10,000 <-> 10000으로 변환합니다.
        // NOTE: addFormatter와 Converter 중에 Converter가 우선순위가 높으므로, 위에서 주석으로 처리하여 포매터 동작을 확인합니다.
        registry.addFormatter(new MyNumberFormatter());
    }
}
