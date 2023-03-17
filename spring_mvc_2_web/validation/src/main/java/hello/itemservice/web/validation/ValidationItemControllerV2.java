package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
@Slf4j
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    // NOTE: 컨트롤러가 요청될 때, WebDataBinder가 생성되고 이 후 메서드 실행 시, 이 검증기부터 적용됩니다.
    @InitBinder
    public void init(WebDataBinder dataBinder) {
        log.info("init binder {}", dataBinder);
        dataBinder.addValidators(itemValidator);
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    // NOTE: 기존에 HashMap으로 관리하던 에러들을 BindingResult로 관리합니다.
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        // 검증 오류 결과 보관 객체 생성
        Map<String, String> errors = new HashMap<>();

        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000까지 허용합니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999까지 허용합니다."));
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야합니다. 현재 값: = " + resultPrice));
            }
        }
        // 검증에 실패하면 다시 입력 폼으로 돌아가도록 설정합니다.
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", errors);
            model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    // NOTE: spring으로 값이 넘어올 때, item 객체에 넣기 전, BindingResult는 입력받은 값을 넣어둡니다.
    // @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        // 검증 오류 결과 보관 객체 생성
        Map<String, String> errors = new HashMap<>();

        if (!StringUtils.hasText(item.getItemName())) {
            // 실패하더라도 data 자체는 잘 들어오게 됩니다. 하지만 잘못된 데이터 전송시 인풋 필드에서 값이 지워지게 되는데
            // 이를 처리하기 위해 서버에서 받은 값을 itemName의 item.getItemName에 넣어줍니다.
            bindingResult.addError(new FieldError("item", "itemName",
                    item.getItemName(), false, null, null,
                    "상품 이름은 필수입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price",
                    item.getPrice(), false, null, null,
                    "가격은 1,000 ~ 1,000,000까지 허용합니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity",
                    item.getQuantity(), false, null, null,
                    "수량은 최대 9,999까지 허용합니다."));
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                // ObjectError는 필드로 값이 넘어오는게 아닌, 여러 필드의 값을 조합하는 역할을 하기 때문에, 필드 에러와 다릅니다.
                bindingResult.addError(new ObjectError("item",
                        null, null,
                        "가격 * 수량의 합은 10,000원 이상이어야합니다. 현재 값: = " + resultPrice));
            }
        }
        // 검증에 실패하면 다시 입력 폼으로 돌아가도록 설정합니다.
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", errors);
            model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    // 오류 메시지 자체를 errors.properties 파일에 설정하여 처리합니다.
    // @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        // 검증 오류 결과 보관 객체 생성
        Map<String, String> errors = new HashMap<>();

        if (!StringUtils.hasText(item.getItemName())) {
            // String 배열을 입력해주어 못찾는 경우 순서대로 찾도록 합니다.
            bindingResult.addError(new FieldError("item", "itemName",
                    item.getItemName(), false, new String[]{"required.item.itemName", "required.default"}, null,
                    null));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price",
                    item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000},
                    null));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity",
                    item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999},
                    null));
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                // ObjectError는 필드로 값이 넘어오는게 아닌, 여러 필드의 값을 조합하는 역할을 하기 때문에, 필드 에러와 다릅니다.
                bindingResult.addError(new ObjectError("item",
                        new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice},
                        "가격 * 수량의 합은 10,000원 이상이어야합니다. 현재 값: = " + resultPrice));
            }
        }
        // 검증에 실패하면 다시 입력 폼으로 돌아가도록 설정합니다.
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", errors);
            model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    // NOTE: addError를 rejectValue로 변경하여 처리합니다. bindingResult가 ObjectName, target을 반환하는 것에 대한 규칙을 적용합니다.
    // @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        // 검증에 실패하면 다시 입력 폼으로 돌아가도록 설정합니다.
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // if (!StringUtils.hasText(item.getItemName())) {
        //     // errors.properties에서 detail한 순서대로 값을 읽어옵니다. Level1이 더 구체적이기 때문에 Level1이 없을 때 Level2가 표현됩니다.
        //     bindingResult.rejectValue("itemName", "required");
        // }
        // 검증 로직: 공백이나 값이 안들어가는 경우 처리하는 방법으로, 위의 세 줄을 대체할 수 있습니다. 이 떄 간단한 비어있거나 띄어쓰기가 있는 경우만 처리합니다.
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    // @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        // 검증 로직
        itemValidator.validate(item, bindingResult);

        // 검증에 실패하면 다시 입력 폼으로 돌아가도록 설정합니다.
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    // NOTE: @Validated는 앞에 등록해둔 Validator를 찾아서 메서드인 support와 validate를 실행합니다.
    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // 검증에 실패하면 다시 입력 폼으로 돌아가도록 설정합니다.
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

