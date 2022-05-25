package hello.itemservice.domain.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * FAST: 빠른 배송
 * NORMAL: 일반 배송
 * SLOW: 느린 배송
 */

@Getter @Setter
@AllArgsConstructor
public class DeliveryCode {
    private String code; //FAST 같은 시스템에서 전달하는 값
    private String displayName; //빠른 배송 같은 고객에게 보여주는 값
}
