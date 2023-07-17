package webserver.http.util;

import java.util.Arrays;
import java.util.Optional;

import webserver.http.Extension;

public class FileUtil {

	public static final String DYNAMIC_PATH = "src/main/resources/templates";
	public static final String STATIC_PATH = "src/main/resources/static";

	public static boolean isFileRequest(String url) {
		String[] splitUrl = url.split("[.]");
		if (splitUrl.length == 1) {
			return false;
		}

		String fileExtension = splitUrl[splitUrl.length - 1];

		return isProvidingExtension(fileExtension);
	}

	private static boolean isProvidingExtension(String extension) {
		Optional<String> findExtension = Arrays.stream(Extension.values())
			.map(t -> t.getValue())
			.filter(s -> s.equals(extension))
			.findFirst();

		return findExtension.isPresent();
	}

	public static String getFilePath(String url) {
		String[] splitUrl = url.split("[.]");
		String fileExtension = splitUrl[splitUrl.length - 1];

		if (Extension.isDynamicFile(fileExtension)) {
			return DYNAMIC_PATH + url;
		}

		return STATIC_PATH + url;
	}
}