package webserver.http;

public enum Extension {

	HTML("html"), CSS("css"), JS("js"),
	ICO("ico"), PNG("png"), JPG("jpg"),
	WOFF("woff"), TTF("ttf");

	private final String value;

	private Extension(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static boolean isDynamicFile(String extension) {
		return extension.equals(HTML.value);
	}
}