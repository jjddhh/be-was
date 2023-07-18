package session;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

public class SessionStorage {

	private static Map<String, String> session = Maps.newHashMap();

	public static boolean isLoginUser(String sessionId) {
		Optional<String> optionalSocialId = Optional.ofNullable(session.get(sessionId));
		return optionalSocialId.isPresent();
	}

	public static void setSession(String sessionId, String userId) {
		session.put(sessionId, userId);
	}

	public static void flush() {
		session = Maps.newHashMap();
	}
}
