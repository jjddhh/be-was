package servlet.domain.user;

import java.util.Map;

import container.Servlet;
import container.annotation.MyMapping;
import db.Database;
import model.user.User;
import model.user.factory.UserFactory;

@MyMapping("/user/create")
public class UserCreateServlet implements Servlet {

	@Override
	public void execute(Map<String, String> model) {
		User user = UserFactory.createUser(model);

		Database.addUser(user);

		System.out.println(Database.findUserById(user.getUserId()));
	}
}
