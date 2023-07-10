package webserver.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HttpUtil 테스트")
class HttpUtilTest {
    // todo: getContent 테스트 작성

    @Test
    @DisplayName("문자열에서 두 번째 요소[url] 가져오기")
    void getUrl() throws IOException {
        // given
        String content = "hello it is just test";

        // when
        String url = HttpUtil.getUrl(content);

        // then
        assertEquals(url, "it");
    }

    @Test
    @DisplayName("문자열에서 두 번째 요소[url] 가져오기 실패")
    void getUrlFailure() throws IOException {
        // given
        String content = "hello it is just test";

        // when
        String url = HttpUtil.getUrl(content);

        // then
        assertNotEquals(url, "is");
    }
}