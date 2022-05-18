package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {
    /* 로컬 메모리에 DB 역할을 하는 저장소 생성 */
    private static final Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L;

    /* 상품 등록시 저장소에 데이터 저장 */
    public Item save(Item item) {
        item.setId(++sequence); //저장될 때마다 상품ID 증가
        store.put(item.getId(), item);

        return item;
    }

    /* 상품 조회시 저장소에서 상품 객체 반환 */
    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    /* 상품 정보 수정시 상품ID로 상품 객체를 찾아서 정보 수정 */
    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);

        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    /* 테스트용 */
    public void clearStore() {
        store.clear();
    }
}
