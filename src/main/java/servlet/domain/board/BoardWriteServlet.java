package servlet.domain.board;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import container.annotation.MyMapping;
import db.BoardDatabase;
import db.UserDatabase;
import model.board.Board;
import model.board.BoardFactory;
import model.user.User;
import servlet.Servlet;
import session.SessionStorage;
import webserver.exception.InvalidRequestException;
import webserver.http.request.Cookies;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.Method;

@MyMapping(url = "/board/write", method = Method.POST)
public class BoardWriteServlet implements Servlet {

	@Override
	public String execute(HttpRequest httpRequest, HttpResponse httpResponse) {
		Cookies cookies = httpRequest.getCookies();
		Map<String, Object> param = httpRequest.getModel();
		String sid = cookies.getCookie("sid");
		if (isLoginUser(sid)) {
			Optional<String> sessionUserId = SessionStorage.getSessionUserId(sid);
			sessionUserId.ifPresent(userId -> {
				Optional<User> userById = UserDatabase.findUserById(userId);
                userById.ifPresent(user -> {
					Board board = BoardFactory.createBoard(param);
					BoardDatabase.save(board);
                });
			});

			return "redirect:/index.html";
		}

		throw InvalidRequestException.Exception;
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
