package webserver.http;

enum Extension {

	HTML("html"), CSS("css"), JS("js"),
	ICO("ico"), PNG("png"), JPG("jpg");

	private final String value;

	private Extension(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}