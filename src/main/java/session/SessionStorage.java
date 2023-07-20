package session;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Maps;

public class SessionStorage {

	private static Map<String, String> session = Maps.newHashMap();

	public static Optional<String> getSessionUserId(String sessionId) {
		Optional<String> optionalUserId = Optional.ofNullable(session.get(sessionId));
		return optionalUserId;
	}

	public static void setSession(String sessionId, String userId) {
		session.put(sessionId, userId);
	}

	public static void flush() {
		session = Maps.newHashMap();
	}
}
