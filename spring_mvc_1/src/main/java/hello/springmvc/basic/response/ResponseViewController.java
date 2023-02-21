package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {

    @RequestMapping("response-view-v1")
    public ModelAndView responseViewV1() {
        ModelAndView mav = new ModelAndView("response/hello")
                .addObject("data", "hello! V1");
        return mav;
    }

    // NOTE: API 사용할 때 썼던 @ResponseBody를 사용하면 View로 가지 않고 API response를 반환합니다.
    @RequestMapping("response-view-v2")
    public String responseViewV2(Model model) {
        model.addAttribute("data", "hello! V2");
        return "response/hello";
    }

    // NOTE: 추천하지 않는 방식
    // NOTE: Controller의 경로와 View의 논리적 이름이 같고 void로 리턴하면 알맞은 경로의 파일을 렌더합니다.
    @RequestMapping("/response/hello")
    public void responseViewV3(Model model) {
        model.addAttribute("data", "hello! V3");
    }
}
