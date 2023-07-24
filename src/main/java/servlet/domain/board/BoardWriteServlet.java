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
import webserver.http.HttpRequest;

@MyMapping("/board/write")
public class BoardWriteServlet implements Servlet {

	@Override
	public String execute(HttpRequest httpRequest) {
		Map<String, String> cookies = httpRequest.getCookies();
		Map<String, String> param = httpRequest.getModel();
		String sid = cookies.get("sid");
		if (isLoginUser(sid)) {
			System.out.println("\n\n\n\n\n\n\n\n\n\n");
			Optional<String> sessionUserId = SessionStorage.getSessionUserId(sid);
			sessionUserId.ifPresent(userId -> {
				Optional<User> userById = UserDatabase.findUserById(userId);
                userById.ifPresent(user -> {
					Board board = BoardFactory.createBoard(param);
					BoardDatabase.save(board);
					System.out.println(board);
                });
			});
		}

		return "redirect:/index.html";
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
