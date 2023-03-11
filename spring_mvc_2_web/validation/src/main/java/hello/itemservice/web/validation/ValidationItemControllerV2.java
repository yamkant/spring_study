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
    @PostMapping("/add")
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

