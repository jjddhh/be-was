package webserver.http.request;

import java.util.Map;

public class Cookies {

	private Map<String, String> cookies;

	public Cookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	public String putCookie(String name, String value) {
		cookies.put(name, value);
        return value;
	}

	public String getCookie(String name) {
        return cookies.get(name);
    }
}
