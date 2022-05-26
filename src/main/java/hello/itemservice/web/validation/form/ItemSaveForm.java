package hello.itemservice.web.validation.form;

import hello.itemservice.domain.item.ItemType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
public class ItemSaveForm {

    @NotBlank
    private String itemName;
    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;
    @NotNull
    @Max(value = 9999)
    private Integer quantity;

    private Boolean open; //판매여부
    private List<String> regions; //등록지역 : 일반문자열로 처리시도
    private ItemType itemType; //상품종류 : Enum 클래스로 처리시도
    private String deliveryCode; //배송방식 : 일반 클래스로 처리시도
}
