package webserver.utils.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FileUtil 테스트")
class FileUtilTest {
    @Test
    @DisplayName("정적 파일 요청임을 확인")
    void isFileRequest() {
        // given
        String url = "/static/test/index.html";

        // when
        boolean fileRequest = FileUtil.isFileRequest(url);

        // then
        assertTrue(fileRequest);
    }

    @Test
    @DisplayName("지원하는 확장자인지 확인")
    void isNotFileRequest() {
        // given
        String url = "/user";

        // when
        boolean fileRequest = FileUtil.isFileRequest(url);

        // then
        assertFalse(fileRequest);
    }

    @Test
    @DisplayName("지원하는 확장자인지 확인")
    void isExtensionProvided() {
        // given
        String url = "/static/test/index.css";

        // when
        boolean fileRequest = FileUtil.isFileRequest(url);

        // then
        assertFalse(fileRequest);
    }
}