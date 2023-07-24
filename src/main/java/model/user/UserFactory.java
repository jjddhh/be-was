package model.user;

import java.util.Map;

import model.user.User;

public class UserFactory {

	private static final String USER_ID = "userId";
	private static final String PASSWORD = "password";
	private static final String NAME = "name";
	private static final String EMAIL = "email";

	public static User createUser(Map<String, String> map) {
		return User.builder()
			.userId(map.get(USER_ID))
			.password(map.get(PASSWORD))
			.name(map.get(NAME))
			.email(map.get(EMAIL))
			.build();
	}
}
