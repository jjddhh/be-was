package webserver.http;

import java.util.Arrays;
import java.util.Optional;

import webserver.http.view.Extension;

public class FileUtil {

	public static boolean isFileRequest(String url) {
		String[] splitUrl = url.split("[.]");
		if (splitUrl.length == 0) {
			return false;
		}

		String fileExtension = splitUrl[splitUrl.length - 1];

		return isProvidedExtension(fileExtension);
	}

	private static boolean isProvidedExtension(String extension) {
		Optional<String> findExtension = Arrays.stream(Extension.values())
			.map(t -> t.getValue())
			.filter(s -> s.equals(extension))
			.findFirst();

		return findExtension.isPresent();
	}
}