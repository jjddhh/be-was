package servlet.domain.html;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import container.annotation.MyMapping;
import db.UserDatabase;
import model.user.User;
import servlet.Servlet;
import session.SessionStorage;
import webserver.http.request.Cookies;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

@MyMapping(url = "/board/write.html")
public class BoardServlet implements Servlet {

	@Override
	public String execute(HttpRequest httpRequest, HttpResponse httpResponse) {

		Cookies cookies = httpRequest.getCookies();
		String sid = cookies.getCookie("sid");
		if (isLoginUser(sid)) {
			return "/board/write.html";
		}

		return "redirect:/user/login.html";
	}

	private boolean isLoginUser(String sid) {
		if(Objects.nonNull(sid)) {
			Optional<String> loginUser = SessionStorage.getSessionUserId(sid);
			if(loginUser.isPresent()) {
				String userId = loginUser.get();
				Optional<User> userById = UserDatabase.findUserById(userId);
				if(userById.isPresent()) {
					return true;
				}
			}
		}

		return false;
	}
}
