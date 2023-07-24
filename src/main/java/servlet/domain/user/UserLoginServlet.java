package servlet.domain.user;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import servlet.Servlet;
import container.annotation.MyMapping;
import db.UserDatabase;
import model.user.User;
import servlet.domain.user.exception.IncorrectPasswordException;
import servlet.domain.user.exception.UserNotExistException;
import session.SessionStorage;
import webserver.http.HttpRequest;

@MyMapping("/user/login")
public class UserLoginServlet implements Servlet {

	private static final Logger logger = LoggerFactory.getLogger(UserLoginServlet.class);

	@Override
	public String execute(HttpRequest httpRequest) {
		Map<String, String> model = httpRequest.getModel();
		String userId = model.get("userId");
		String password = model.get("password");

		Optional<User> userById = UserDatabase.findUserById(userId);

		try {
			User user = getUser(userById);
			verifyPassword(password, user.getPassword());
		} catch (IncorrectPasswordException | UserNotExistException exception) {
			logger.info(exception.getMessage());
			return "/user/login_failed.html";
		}

		String sessionId = UUID.randomUUID().toString();
		SessionStorage.setSession(sessionId,userId);
		model.put("Cookie", sessionId);
		logger.info("user sessionId: {}", sessionId);

		return "redirect:/index.html";
	}

	private User getUser(Optional<User> userById) {
		User user = userById.orElseThrow(() -> UserNotExistException.Exception);
		return user;
	}

	private void verifyPassword(String password, String dbPassword) {
		if (!dbPassword.equals(password)) {
			throw IncorrectPasswordException.Exception;
		}
	}
}
