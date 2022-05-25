package hello.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *  롬복을 통해 getter와 setter 할당
 *  @Data 롬복은 예측하지 못한 상황을 유발하기 때문에 되도록 사용 X
 * */
@Getter @Setter
public class Item {
    /**
     *  상품 객체의 필드
     *  Integer 타입으로 작성하여 null을 허용한다
     *  */
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    private Boolean open; //판매여부
    private List<String> regions; //등록지역 : 일반문자열로 처리시도
    private ItemType itemType; //상품종류 : Enum 클래스로 처리시도
    private String deliveryCode; //배송방식 : 일반 클래스로 처리시도

   public Item() {
    }

    /**
     *  ID는 자동으로 값이 할당되도록 할 것이기 떄문에( PK 역할 )
     *  ID를 제외한 필드를 할당받는 생성자를 생성
     * */
    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
