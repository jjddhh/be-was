package webserver.http.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.RequestHandler;
import webserver.exception.InvalidRequestException;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpUtil {

	private static final String PATH_PARAM_SEPARATOR = "[?]";
	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

	public static String getContent(BufferedReader reader) throws IOException {
		StringBuilder content = new StringBuilder();
		String line;
		int contentLength = 0;

		while ((line = reader.readLine()) != null && !line.isEmpty()) {
			content.append(line).append(System.lineSeparator());
			logger.debug(line);

			if (line.startsWith("Content-Length: ")) {
				contentLength = Integer.parseInt(line.split(": ")[1]);
			}
		}

		if (contentLength > 0) {
			content.append('\n');

			char[] bodyChars = new char[contentLength];
			reader.read(bodyChars, 0, contentLength);
			String body = new String(bodyChars);

			content.append(body);
		}

		verifyContent(content);

		return content.toString();
	}

	public static String extractBody(String content) {
		String decodedContent = URLDecoder.decode(content, StandardCharsets.UTF_8);
		String[] lines = decodedContent.split("\\r?\\n");

		int emptyLineIndex = 0;
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].isEmpty()) {
				emptyLineIndex = i;
				break;
			}
		}

		StringBuilder bodyBuilder = new StringBuilder();
		for (int i = emptyLineIndex + 1; i < lines.length; i++) {
			bodyBuilder.append(lines[i]);
		}

		return bodyBuilder.toString();
	}

	public static String extractHeader(String content) {
		String[] lines = content.split("\\r?\\n");

		int emptyLineIndex = 0;
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].isEmpty()) {
				emptyLineIndex = i;
				break;
			}

			emptyLineIndex = i;
		}

		StringBuilder headerBuilder = new StringBuilder();
		for (int i = 0; i <= emptyLineIndex; i++) {
			headerBuilder.append(lines[i]).append("\n");
		}

		return headerBuilder.toString().trim();
	}

	private static void verifyContent(StringBuilder content) {
		if (content.toString().isEmpty()) {
			throw InvalidRequestException.Exception;
		}
	}

	public static String getPathParam(String content) throws IOException {
		String[] splitContent = content.split(" ");
		String pathParam = splitContent[1];

		return pathParam;
	}

	public static String getPath(String pathParam) {
		String[] pathAndParam = getDecodedPathAndParam(pathParam);
		String path = pathAndParam[0];
		return path;
	}

	public static String getParam(String pathParam) {
		String[] pathAndParam = getDecodedPathAndParam(pathParam);

		if (pathAndParam.length == 1) {
			return null;
		}

		String param = pathAndParam[1];
		return param;
	}

	private static String[] getDecodedPathAndParam(final String pathParam) {
		String decodedPathParam = URLDecoder.decode(pathParam, StandardCharsets.UTF_8);
		String[] pathAndParam = decodedPathParam.split(PATH_PARAM_SEPARATOR);
		return pathAndParam;
	}

	public static String getContentType(final String headers) {
		Map<String, String> headerMap = parseHeaders(headers);

		String acceptHeader = headerMap.get("Accept");
		if (Objects.nonNull(acceptHeader)) {
			String[] values = acceptHeader.split(",");
			return values[0];
		}

		throw InvalidRequestException.Exception;
	}

	private static Map<String, String> parseHeaders(final String headers) {
		Map<String, String> headerMap = new HashMap<>();
		String[] lines = headers.split("\n");

		for (String line : lines) {
			String[] parts = line.split(": ", 2);
			if (parts.length == 2) {
				headerMap.put(parts[0], parts[1]);
			}
		}
		return headerMap;
	}

	public static String getMethod(String header) {
		String[] splitContent = header.split(" ");
		String method = splitContent[0];

		return method;
	}

	public static Map<String, String> getCookies(final String headers) {
		Map<String, String> headerMap = parseHeaders(headers);
		String cookies = headerMap.get("Cookie");

		if(Objects.isNull(cookies)) return null;

		return Arrays.stream(cookies.split(";"))
				.map(String::trim)
				.map(s -> s.split("="))
				.collect(Collectors.toMap(
						a -> a[0],  // key
						a -> a[1]   // value
				));
	}
}