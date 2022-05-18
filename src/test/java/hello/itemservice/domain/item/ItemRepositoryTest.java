package hello.itemservice.domain.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    /**
     * 다음 테스트에 저장소에 남은 데이터의 영향을 주지 않도록 하기 위해
     * @AfterEach 를 통해 저장소의 데이터를 테스트가 끝날 때마다 날려준다
     **/
    @AfterEach
    void afterEach() {
        itemRepository.clearStore();
    }

    @Test
    public void 상품등록() throws Exception {
        //given : 상품 객체 생성
        Item item = new Item("itemA", 10000, 10);

        //when : 생성한 상품 저장소에 저장
        Item savedItem = itemRepository.save(item);

        //then : 저장소에서 저장한 상품의 아이디로 해당 상품을 찾아와 저장된 상품과 동일한지 검증
        Item findItem = itemRepository.findById(item.getId());
        Assertions.assertThat(findItem).isEqualTo(savedItem);
    }

    @Test
    public void 상품조회() throws Exception {
        //given : 상품 2개 생성 및 저장소에 모두 저장
        Item item1 = new Item("item1", 10000, 10);
        Item item2 = new Item("item2", 20000, 20);

        itemRepository.save(item1);
        itemRepository.save(item2);

        //when : 저장소에 저장된 상품 모두 조회
        List<Item> items = itemRepository.findAll();

        //then : 조회한 저장소의 상품의 개수가 2개가 맞는지, 저장한 상품들과 동일한지 검증
        Assertions.assertThat(items.size()).isEqualTo(2);
        Assertions.assertThat(items).contains(item1, item2);
    }

    @Test
    public void 상품수정() throws Exception {
        //given : 상품 생성 및 등록
        Item item = new Item("item1", 10000, 10);

        Item savedItem = itemRepository.save(item);
        Long itemId = savedItem.getId();

        //when : 수정할 상품의 정보를 담은 객체를 생성하여 저장소의 상품 수정
        Item updateParam = new Item("item2", 20000, 30);
        itemRepository.update(itemId, updateParam);

        Item findItem = itemRepository.findById(itemId);

        //then : 수정된 상품의 정보가 올바른지 검증
        Assertions.assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
        Assertions.assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
        Assertions.assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());
    }
}