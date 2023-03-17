package hello.itemservice.web.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

// NOTE: @ModelAttribute vs @RequestBody
// - ModelAttribute는 필드 단위로 정교하게 각각에 대해 오류가 적용됩니다.
// - 하지만, RequestBody의 경우 MessageConverter는 우선적으로 ItemSaveForm 객체 형식으로 변경하는 과정이 우선적이기 때문에,
//   먼저 변경되지 않는다면, 다음 단계를 진행할 수 없습니다.

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

    @ResponseBody
    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {

        log.info("API 컨트롤러 호출");

        // NOTE: @RequestBody의 값을 통해 ItemSaveForm에 대입해봤는데 오류가 발생했고,
        //       해당 오류는 BindingResult에 바인딩되고, 해당 오류를 클라이언트에 돌려줍니다.
        // NOTE: 실제 개발 시에는 반환에 필요한 데이터들만 선별하여 반환합니다.
        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생 errors={}", bindingResult);
            return bindingResult.getAllErrors();
        }

        log.info("성공 로직 실행");
        return form;
    }
}
