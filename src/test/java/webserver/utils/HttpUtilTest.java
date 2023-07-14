package webserver.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Map;

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
		String actualUrl = HttpUtil.getPathParam(content);

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
		String actualUrl = HttpUtil.getPathParam(content);

		// then
		assertNotEquals(expectedUrl, actualUrl);
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
		assertEquals(value1, model.get(key1));
		assertEquals(value2, model.get(key2));
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
		assertNull(model.get(strangeKey));
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
		assertEquals(path, actual);
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
		assertEquals(param, actual);
	}
}