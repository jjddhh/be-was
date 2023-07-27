package model.user;

import java.util.Map;

public class UserFactory {

	private static final String USER_ID = "userId";
	private static final String PASSWORD = "password";
	private static final String NAME = "name";
	private static final String EMAIL = "email";

	public static User createUser(Map<String, Object> map) {
		return User.builder()
			.userId((String)map.get(USER_ID))
			.password((String)map.get(PASSWORD))
			.name((String)map.get(NAME))
			.email((String)map.get(EMAIL))
			.build();
	}
}
