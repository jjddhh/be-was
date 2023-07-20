package servlet.domain.user;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import servlet.Servlet;
import container.annotation.MyMapping;
import db.Database;
import lock.NamedLock;
import model.user.User;
import model.user.factory.UserFactory;
import servlet.domain.user.exception.AlreadyExistUserException;
import webserver.http.HttpRequest;

@MyMapping("/user/create")
public class UserCreateServlet implements Servlet {

	private static final Logger logger = LoggerFactory.getLogger(UserCreateServlet.class);

	@Override
	public String execute(HttpRequest httpRequest) {
		Map<String, String> model = httpRequest.getModel();
		User user = UserFactory.createUser(model);

		Lock lock = NamedLock.getLock(user.getName());

		lock.lock();
		try {
			Optional<User> findUser = Database.findUserById(user.getUserId());
			verifyCreateUser(findUser);
			Database.addUser(user);

			logger.info("Registered UserId: {}", Database.findUserById(user.getUserId()));
		} finally {
			lock.unlock();
		}

		return "redirect:/index.html";
	}

	private void verifyCreateUser(Optional<User> findUser) {
		if (findUser.isPresent()) {
			throw AlreadyExistUserException.Exception;
		}
	}
}