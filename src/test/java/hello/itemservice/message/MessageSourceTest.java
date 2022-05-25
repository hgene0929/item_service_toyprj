package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

@SpringBootTest
public class MessageSourceTest {
    @Autowired
    MessageSource ms;

    /* 저장된 메시지 출력확인 */
    @Test
    public void 메시지() throws Exception {
        String result = ms.getMessage("hello", null, null);
        Assertions.assertThat(result).isEqualTo("안녕");
    }

    /* 호출한 메시지가 없는 경우 예외확인 */
    @Test
    public void 메시지가없는경우() throws Exception {
        Assertions.assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    /* 호출한 메시지가 없는 경우에도 기본 메시지를 지정해놓은 경우, 기본 메시지 출력 */
    @Test
    public void 디폴트메시지() throws Exception {
        String result = ms.getMessage("no_code", null, "기본 메시지", null);
        Assertions.assertThat(result).isEqualTo("기본 메시지");
    }

    /* 메시지의 {0} 부분을 매개변수를 전달하여 치환 */
    @Test
    public void 매개변수사용() throws Exception {
        String result = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
        Assertions.assertThat(result).isEqualTo("안녕 Spring");
    }

    @Test
    public void 국제화한국() throws Exception {
        Assertions.assertThat(ms.getMessage("hello", null, null))
                .isEqualTo("안녕");
        Assertions.assertThat(ms.getMessage("hello", null, Locale.KOREA))
                .isEqualTo("안녕");
    }
    
    @Test
    public void 국제화영어권() throws Exception {
        Assertions.assertThat(ms.getMessage("hello", null, Locale.ENGLISH))
                .isEqualTo("hello");
    }
}
