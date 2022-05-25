package hello.itemservice.web.form;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.*;

@Controller
@RequestMapping("/form/items") //중복되는 URL 미리 매핑
@Slf4j
public class FormItemController {
    /**
     *  의존관계 주입
     * @RequiredArgsContructor 롬복으로 대체 가능 : 객체가 1개 뿐이기 때문이다
     **/
    private final ItemRepository itemRepository;

    @Autowired
    public FormItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     *  상품 목록 조회
     *  URI = /list/items
    **/
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "form/items";
    }

    /**
     *  테스트용 데이터
     *  @PostConstruct : 해당 빈의 의존관계가 모두 주입되고 나면 초기화 용도로 호출된다
     **/
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }

    /**
     *  상품 상세 정보 조회
     *  URI = /basic/items/{itemId}
     *  @PathVariable로 경로변수 받음
     **/
    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "form/item";
    }

    /**
     *  상품 등록 폼
     *  URI = /basic/items/add
     **/
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "form/addForm";
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
     *  @ModelAttribute 를 통해 요청 파라미터 조회
     **/
    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        log.info("item.itemType={}", item.getItemType());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    /**
     *  상품 수정 폼
     *  URI = /basic/items/{itemId}/edit
     * @PathVariable 사용
     **/
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId,
                           Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "form/editForm";
    }

    /**
     *  상품 수정
     *  URI = /basic/items/{itemId}/edit
     * @PathVariable 사용
     * @ModelAttribute 를 통해 요청 파라미터를 조회
     **/
     @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId,
                       @ModelAttribute Item item) {
         itemRepository.update(itemId, item);

         return "redirect:/form/items/{itemId}";
     }

     @ModelAttribute("regions")
    public Map<String, String> regions() {
         Map<String, String> regions = new LinkedHashMap<>();
         regions.put("SEOUL", "서울");
         regions.put("BUSAN", "부산");
         regions.put("JEJU", "제주");

         return regions;
     }

     @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
         return ItemType.values();
     }

     @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
         List<DeliveryCode> deliveryCodes = new ArrayList<>();
         deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
         deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
         deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));

         return deliveryCodes;
     }
}
