package session;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

public class SessionStorage {

	private static Map<String, String> session = Maps.newHashMap();

	public static boolean isLoginUser(String userId) {
		Optional<String> optionalSocialId = Optional.ofNullable(session.get(userId));
		return optionalSocialId.isPresent();
	}

	public static void setSession(String sessionId, String socialId) {
		session.put(sessionId, socialId);
	}
}
