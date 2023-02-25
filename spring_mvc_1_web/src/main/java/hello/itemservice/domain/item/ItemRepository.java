package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    // NOTE: static을 사용하여 객체가 생성될 때 정의를 한 번만 실행합니다.
    // 실제로는 HashMap을 사용하면 안됩니다.
    // -> 멀티 쓰레드 환경에서 여러 개의 쓰레드가 해당 객체에 동시에 접근할 수 있게 됨.
    // -> ConcurrentHashMap / atomic long 등 다른 자료형을 사용해야합니다.
    private static final Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L;

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    // NOTE: 정석으로 하기 위해서는, ItemParameterDTO 등을 객체로 만들고, 업데이트하는 값들만 객체에 넣어서 사용합니다.
    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }

}
