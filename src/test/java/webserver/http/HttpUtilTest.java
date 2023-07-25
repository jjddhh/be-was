package webserver.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

import webserver.exception.InvalidRequestException;
import webserver.http.util.HttpUtil;

@DisplayName("HttpUtil 테스트")
class HttpUtilTest {

	@Test
	@DisplayName("Body 없는 요청 Content 받아오기")
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

//	@Test
	@DisplayName("Body 있는 요청 Content 받아오기")
	public void getContentIncludingBody() throws IOException {
		// given
		String body = "It's body";
		String expectedContent = "Hello, World!" + System.lineSeparator()
			+ "Content-Length: " + body.getBytes().length + System.lineSeparator()
			+ System.lineSeparator()
			+ body;

		InputStream inputStream = new ByteArrayInputStream(expectedContent.getBytes("UTF-8"));
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

		// when
		String actualContent = HttpUtil.getContent(bufferedReader);
		System.out.println();
		byte[] bytes = actualContent.getBytes();
		byte[] bytes1 = expectedContent.getBytes();
		System.out.println(Arrays.equals(bytes1, bytes));

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

		// when then
		assertThatThrownBy(() -> HttpUtil.getContent(bufferedReader))
			.isInstanceOf(InvalidRequestException.class);
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

	@Test
	@DisplayName("getCookie 성공")
	void getCookie() {
		// given
		String header = "GET /index.html HTTP/1.1\n" +
			"Host: localhost:8080\n" +
			"Connection: keep-alive\n" +
			"Cookie: sid=123456";

		// when
		Map<String, String> cookies = HttpUtil.getCookies(header);
		String sid = cookies.get("sid");

		// then
		assertThat(sid).isEqualTo("123456");
	}
	
	@Test
	@DisplayName("Cookie 없는 경우")
	void getEmptyCookie() {
	    // given
		String header = "GET /index.html HTTP/1.1\n" +
			"Host: localhost:8080\n" +
			"Connection: keep-alive\n";

		// when
		Map<String, String> cookies = HttpUtil.getCookies(header);

		// then
		assertThat(cookies).isEqualTo(null);
	}
}