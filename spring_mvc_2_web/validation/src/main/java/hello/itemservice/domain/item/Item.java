package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
// NOTE: 기능이 있지만, 권장하지 않는 기능입니다.
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "총합이 10000원을 넘게 입력해주세요.")
public class Item {

    // 수정시에 요구사항이 아래와 같이 변경된다면?
    // 1. id는 수정시 null이면 안됩니다. -> id 필드에 @NotNull 추가
    // 2. 수량은 등록할 때 제한이 있었지만, 수정할 떄는 제한이 없습니다. -> quantity 필드에 @Max 제거
    // 하지만, 위의 두 방식으로 수정하면 등록 상황에서는 의도대로 동작하지 않는 side-effect가 생깁니다.
    // 이를 해결하기 위해 groups를 사용하여 각 경우에 해당하는 annotation을 사용합니다.

    @NotNull(groups = UpdateCheck.class)
    private Long id;
    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;
    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Max(value = 9999, groups = {SaveCheck.class})
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
