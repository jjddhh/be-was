package webserver.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HttpUtil 테스트")
class HttpUtilTest {

    @Test
    @DisplayName("요청 Content 받아오기")
    public void getContent() throws IOException {
        // given
        String expectedContent = "Hello, World!" + System.lineSeparator();
        InputStream inputStream = new ByteArrayInputStream(expectedContent.getBytes("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        // when
        String actualContent = HttpUtil.getContent(bufferedReader);

        // then
        assertEquals(expectedContent, actualContent);
    }

    @Test
    @DisplayName("빈 Content가 들어왔을 경우")
    public void getContentWithEmptyInput() throws IOException {
        // given
        String expectedContent = System.lineSeparator();
        InputStream inputStream = new ByteArrayInputStream(expectedContent.getBytes("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        // when
        String actualContent = HttpUtil.getContent(bufferedReader);

        // then
        assertEquals(expectedContent, actualContent);
    }

    @Test
    @DisplayName("요청 Content가 여러줄일 경우")
    public void getContentWithMultipleLinesInput() throws IOException {
        // given
        String expectedContent = "Hello," + System.lineSeparator() + "World!" + System.lineSeparator();
        InputStream inputStream = new ByteArrayInputStream(expectedContent.getBytes("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        // when
        String actualContent = HttpUtil.getContent(bufferedReader);

        // then
        assertEquals(expectedContent, actualContent);
    }

    @Test
    @DisplayName("문자열에서 두 번째 요소[url] 가져오기")
    void getUrl() throws IOException {
        // given
        String content = "hello it is just test";
        String expectedUrl = "it";

        // when
        String actualUrl = HttpUtil.getUrl(content);

        // then
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    @DisplayName("문자열에서 두 번째 요소[url] 가져오기 실패")
    void getUrlFailure() throws IOException {
        // given
        String content = "hello it is just test";
        String expectedUrl = "is";

        // when
        String actualUrl = HttpUtil.getUrl(content);

        // then
        assertNotEquals(expectedUrl, actualUrl);
    }
}