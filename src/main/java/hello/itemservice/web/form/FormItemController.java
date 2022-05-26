package hello.itemservice.web.form;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import hello.itemservice.web.validation.ItemValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.*;

@Controller
@RequestMapping("/form/items") //중복되는 URL 미리 매핑
@Slf4j
@RequiredArgsConstructor
public class FormItemController {
    /**
     *  의존관계 주입
     * @RequiredArgsContructor 롬복으로 대체 가능
     **/
    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

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
     *  직접 등록한 검증 로직 사용
     *
    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {
        // 타임리프 체크박스 결과값을 확인하기 위한 로그
        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        log.info("item.itemType={}", item.getItemType());

        // 검증 오류 결과를 보관
        Map<String, String> errors = new HashMap<>();

        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            errors.put("itemName", "상품 이름은 필수입니다.");
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            errors.put("quantity", "수량은 최대 9,999까지 허용합니다.");
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. " +
                        "현재 값 = " + resultPrice);
            }
        }

        // 검증을 통과하지 못한 경우 다시 입력폼으로 돌아갈 수 있도록 설정
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "form/addForm";
        }

        // 메인 로직
        Item savedItem = itemRepository.save(item);

        // 검증을 통과한 경우우
       redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // 타임리프 체크박스 결과값을 확인하기 위한 로그
        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        log.info("item.itemType={}", item.getItemType());

        // BindingResult 에 저장된 검증 대상 target 확인
        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.rejectValue("itemName", "required");
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.rejectValue("price",
                    "range", new Object[]{1000, 1000000}, null);
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.rejectValue("quantity",
                    "max", new Object[]{9999}, null);
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice},
                        null);
            }
        }

        // 검증을 통과하지 못한 경우 다시 입력폼으로 돌아갈 수 있도록 설정
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "form/addForm";
        }

        // 메인 로직
        Item savedItem = itemRepository.save(item);

        // 검증을 통과한 경우
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    } */

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {

        itemValidator.validate(item, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "form/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/form/items/{itemId}";
    }

    /* WebDataBinder 사용
    @InitBinder
    public void init(WebDataBinder dataBinder) {
        log.info("init binder {}", dataBinder);
        dataBinder.addValidators(itemValidator);
    }
    */

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
