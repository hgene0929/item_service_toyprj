package hello.itemservice.domain.item;

import lombok.Getter;

@Getter
public enum ItemType {
    BOOK("도서"), FOOD("식품"), ETC("기타");

    /* 설명을 위해 description 필드 추가 */
    private final String description;

    ItemType(String description) {
        this.description = description;
    }
}
