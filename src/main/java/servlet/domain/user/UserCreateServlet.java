package servlet.domain.user;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

import container.Servlet;
import container.annotation.MyMapping;
import db.Database;
import lock.NamedLock;
import model.user.User;
import model.user.factory.UserFactory;
import servlet.domain.user.exception.AlreadyExistUserException;

//@ResponseBody
@MyMapping("/user/create")
public class UserCreateServlet implements Servlet {

	@Override
	public String execute(Map<String, String> model) {
		User user = UserFactory.createUser(model);

		NamedLock namedLocks = new NamedLock();
		Lock lock = namedLocks.getLock(user.getName());

		lock.lock();
		try {
			Optional<User> findUser = Database.findUserById(user.getUserId());
			verifyCreateUser(findUser);
			Database.addUser(user);

			System.out.println(Database.findUserById(user.getUserId()));
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