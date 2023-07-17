package webserver.http;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

import webserver.exception.InvalidRequestException;
import webserver.http.util.HttpUtil;

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
		assertThat(actualContent).isEqualTo(expectedContent);
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
		assertThat(actualContent).isEqualTo(expectedContent);
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
		assertThat(actualContent).isEqualTo(expectedContent);
	}

	@Test
	@DisplayName("empty content 검증")
	void verifyEmptyContent() throws UnsupportedEncodingException {
		// given
		String emptyContent = "";
		InputStream inputStream = new ByteArrayInputStream(emptyContent.getBytes("UTF-8"));
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

		// when then
		assertThatThrownBy(() -> HttpUtil.getContent(bufferedReader))
			.isInstanceOf(InvalidRequestException.class);
	}

	@Test
	@DisplayName("문자열에서 두 번째 요소[url] 가져오기")
	void getUrl() throws IOException {
		// given
		String content = "hello it is just test";
		String expectedUrl = "it";

		// when
		String actualUrl = HttpUtil.getPathParam(content);

		// then
		assertThat(actualUrl).isEqualTo(expectedUrl);
	}

	@Test
	@DisplayName("문자열에서 두 번째 요소[url] 가져오기 실패")
	void getUrlFailure() throws IOException {
		// given
		String content = "hello it is just test";
		String expectedUrl = "is";

		// when
		String actualUrl = HttpUtil.getPathParam(content);

		// then
		assertThat(actualUrl).isNotEqualTo(expectedUrl);
	}

	@Test
	@DisplayName("param 문자열에서 param pair을 Map으로 분리해서 반환")
	void getModel() {
		// given
		String key1 = "key1";
		String value1 = "test";
		String key2 = "key2";
		String value2 = "ing";
		String param = key1 + "=" + value1 + "&" + key2 + "=" + value2;
		// when
		Map<String, String> model = HttpUtil.getModel(param);

		// then
		SoftAssertions.assertSoftly(softAssertions -> {
			softAssertions.assertThat(model.get(key1)).isEqualTo(value1);
			softAssertions.assertThat(model.get(key2)).isEqualTo(value2);
		});
	}

	@Test
	@DisplayName("param 문자열에 없는값 가져오려할 시, null 반환")
	void getModelFailure() {
		// given
		String key1 = "key1";
		String value1 = "test";
		String key2 = "key2";
		String value2 = "ing";
		String param = key1 + "=" + value1 + "&" + key2 + "=" + value2;

		String strangeKey = "strangeKey";

		// when
		Map<String, String> model = HttpUtil.getModel(param);

		// then
		assertThat(model.get(strangeKey)).isNull();
	}

	@Test
	@DisplayName("pathParam 에서 path 분리")
	void getPath() {
		// given
		String path = "/create";
		String param = "userId=javajigi&password=password&name=tester&email=javajigi@slipp.net";
		String pathParam = path + "?" + param;

		// when
		String actual = HttpUtil.getPath(pathParam);

		// then
		assertThat(actual).isEqualTo(path);
	}

	@Test
	@DisplayName("pathParam 에서 param 분리")
	void getParam() {
		// given
		String path = "/create";
		String param = "userId=javajigi&password=password&name=tester&email=javajigi@slipp.net";
		String pathParam = param + "?" + param;

		// when
		String actual = HttpUtil.getParam(pathParam);

		// then
		assertThat(actual).isEqualTo(param);
	}

	@Test
	@DisplayName("param 없는 상태로 getParam 요청시 null 반환")
	void getNullParam() {
		// given
		String path = "/create";
		// String pathParam = param + "?" + param;

		// when
		String actual = HttpUtil.getParam(path);

		// then
		assertThat(actual).isEqualTo(null);
	}
	
	@Test
	@DisplayName("null param으로 getModel 요청시 empty map 반환")
	void getEmptyMap() {
	    // given
		String nullParam = null;
		String emptyParam = "";
	    
	    // when
		Map<String, String> nullModel = HttpUtil.getModel(nullParam);
		Map<String, String> emptyModel = HttpUtil.getModel(emptyParam);

		// then
		SoftAssertions.assertSoftly(softAssertions -> {
			softAssertions.assertThat(nullModel.size()).isEqualTo(0);
			softAssertions.assertThat(emptyModel.size()).isEqualTo(0);
		});
	}

	@Test
	@DisplayName("param 존재하지 않을 경우")
	void hasNoParam() {
		// given
		String path = "/create";

		// when
		String actual = HttpUtil.getParam(path);

		// then
		assertThat(actual).isEqualTo(null);
	}

	@Test
	@DisplayName("content type 가져오기")
	void getContentType() {
	    // given
		String header = "GET /index.html HTTP/1.1\n" +
			"Host: localhost:8080\n" +
			"Connection: keep-alive\n" +
			"Accept: */*";

	    // when
		String contentType = HttpUtil.getContentType(header);

		// then
		assertThat(contentType).isEqualTo("*/*");
	}

	@Test
	@DisplayName("Accept 필드값 존재하지 않을 경우")
	void getContentTypeFailure() {
		// given

		String header = "GET /index.html HTTP/1.1\n" +
			"Host: localhost:8080\n" +
			"Connection: keep-alive\n";

		// when then
		assertThatThrownBy(() -> HttpUtil.getContentType(header))
			.isInstanceOf(InvalidRequestException.class);
	}
}