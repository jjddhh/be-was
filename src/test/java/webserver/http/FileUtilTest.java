package webserver.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import webserver.http.util.FileUtil;

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
    @DisplayName("파일 요청이 아닐 경우")
    void isNotFileRequest() {
        // given
        String url = "/user/register";

        // when
        boolean fileRequest = FileUtil.isFileRequest(url);

        // then
        assertThat(fileRequest).isFalse();
    }

    @Test
    @DisplayName("지원하는 확장자인지 확인")
    void isExtensionProvided() {
        // given
        String url = "/static/css/index.css";

        // when
        boolean fileRequest = FileUtil.isFileRequest(url);

        // then
        assertTrue(fileRequest);
    }

    @Test
    @DisplayName("동적 파일 요청시 그에 맞는 경로 반환")
    public void getDynamicFilePath() {
        // given
        String url = "index.html";

        // when
        String filePath = FileUtil.getFilePath(url);

        // then
        assertThat(filePath).isEqualTo(FileUtil.DYNAMIC_PATH + url);
    }

    @Test
    @DisplayName("정적 파일 요청시 그에 맞는 경로 반환")
    public void getStaticFilePath() {
        // given
        String url = "deco.css";

        // when
        String filePath = FileUtil.getFilePath(url);

        // then
        assertThat(filePath).isEqualTo(FileUtil.STATIC_PATH + url);
    }
}