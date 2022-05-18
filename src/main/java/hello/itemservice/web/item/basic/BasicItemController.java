package hello.itemservice.web.item.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items") //중복되는 URL 설정
@RequiredArgsConstructor //의존관계 주입
public class BasicItemController {
    /* 의존관계 주입 */
    private final ItemRepository itemRepository;

    /**
     *  상품 목록 조회
     *  URI = /basic/items
    * */
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "basic/items";
    }

    /**
     *  테스트용 데이터
     * */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }

    /**
     *  상품 상세 정보 조회
     *  URI = /basic/items/{itemId}
     * @PathVariable로 경로변수 받음
     * */
    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/item";
    }

    /**
     *  상품 등록 폼
     *  URI = /basic/items/add
     * */
    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    /**
     *  상품 등록
     *  URI = /basic/items/add
     * @RequestParam 을 통해 요청 파라미터 조회
     *
    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model) {

        Item item = new Item(itemName, price, quantity);
        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    } */

    /**
     *  상품 등록
     *  URI = /basic/items/add
     * @ModelAttribute 를 통해 요청 파라미터 조회
     * */
     @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item,
                            Model model) {

         itemRepository.save(item);
         model.addAttribute("item", item);

         return "redirect:/basic/items/" + item.getId();
     }

    /**
     *  상품 수정 폼
     *  URI = /basic/items/{itemId}/edit
     * @PathVariable 사용
     * */
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId,
                           Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/editForm";
    }

    /**
     *  상품 수정
     *  URI = /basic/items/{itemId}/edit
     * @PathVariable 사용
     * @ModelAttribute 를 통해 요청 파라미터를 조회
     * */
     @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId,
                       @ModelAttribute Item item) {
         itemRepository.update(itemId, item);

         return "redirect:/basic/items/{itemId}";
     }
}
