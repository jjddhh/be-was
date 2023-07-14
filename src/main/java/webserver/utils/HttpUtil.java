package webserver.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.RequestHandler;
import webserver.exception.InvalidRequestException;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpUtil {

	private static final String PATH_PARAM_SEPARATOR = "[?]";
	private static final String INTER_PARAM_SEPARATOR = "[&]";
	private static final String INTRA_PARAM_SEPARATOR = "[=]";
	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

	public static String getContent(BufferedReader reader) throws IOException {
		StringBuilder content = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			content.append(line).append(System.lineSeparator());

			if (line.isEmpty()) {
				break;
			}

			logger.debug(line);
		}

		verifyContent(content);

		return content.toString();
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

	public static Map<String, String> getModel(String param) {
		if (hasNoParam(param)) {
			return new HashMap<>();
		}

		String[] parsedParam = param.split(INTER_PARAM_SEPARATOR);
		Map<String, String> queryPair = new HashMap<>();
		for (String pair : parsedParam) {
			String[] splitPair = pair.split(INTRA_PARAM_SEPARATOR);
			queryPair.put(splitPair[0], splitPair[1]);
		}

		return queryPair;
	}

	private static boolean hasNoParam(String param) {
		return Objects.isNull(param);
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

	private static String[] getDecodedPathAndParam(String pathParam) {
		String decodedPathParam = URLDecoder.decode(pathParam, StandardCharsets.UTF_8);
		String[] pathAndParam = decodedPathParam.split(PATH_PARAM_SEPARATOR);
		return pathAndParam;
	}
}