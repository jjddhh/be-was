package webserver.http;

import java.io.IOException;
import java.util.Map;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestTest {

    @Test
    @DisplayName("GET Request Param 파싱해서 model 반환")
    void parseQueryString() throws IOException {
        // given
        String content = "GET /index.html?userId=javajigi&password=password&name=tester&email=javajigi@slipp.net HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*";

        HttpRequest httpRequest = new HttpRequest(content);

        // when
        Map<String, String> model = httpRequest.getModel();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(model.size()).isEqualTo(4);
            softAssertions.assertThat(model.get("userId")).isEqualTo("javajigi");
            softAssertions.assertThat(model.get("password")).isEqualTo("password");
            softAssertions.assertThat(model.get("name")).isEqualTo("tester");
            softAssertions.assertThat(model.get("email")).isEqualTo("javajigi@slipp.net");
        });
    }

    @Test
    @DisplayName("POST Request Body 파싱해서 model 반환")
    void parseBody() throws IOException {
        // given
        String content = "POST /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "\n" +
                "userId=javajigi&password=password&name=tester&email=javajigi@slipp.net";

        HttpRequest httpRequest = new HttpRequest(content);

        // when
        Map<String, String> model = httpRequest.getModel();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(model.size()).isEqualTo(4);
            softAssertions.assertThat(model.get("userId")).isEqualTo("javajigi");
            softAssertions.assertThat(model.get("password")).isEqualTo("password");
            softAssertions.assertThat(model.get("name")).isEqualTo("tester");
            softAssertions.assertThat(model.get("email")).isEqualTo("javajigi@slipp.net");
        });
    }
}