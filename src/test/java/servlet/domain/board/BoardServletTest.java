package servlet.domain.board;

import java.util.HashMap;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import db.BoardDatabase;
import db.UserDatabase;
import model.board.Board;
import model.board.BoardFactory;
import model.user.User;
import session.SessionStorage;
import webserver.http.request.Cookies;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

class BoardServletTest {

	@BeforeEach
	void init() {
		UserDatabase.flush();
		BoardDatabase.flush();
		SessionStorage.flush();
	}

	@Test
	@DisplayName("게시판 글 작성 페이지")
	void loginUser() {
	    // given
		HashMap<String, Object> model = new HashMap<>();
		model.put("title", "test title");
		model.put("content", "test content");
		model.put("writer", "test writer");
		Board board = BoardFactory.createBoard(model);
		BoardDatabase.save(board);

		HashMap<String, String> cookieMap = new HashMap<>();
		cookieMap.put("sid", "SessionId");
		Cookies cookies = new Cookies(cookieMap);

		addUser();
		SessionStorage.setSession("SessionId", "userId");

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setCookies(cookies);
		httpRequest.setModel(model);

		HttpResponse httpResponse = new HttpResponse();

		BoardServlet boardServlet = new BoardServlet();

		// when
		String actual = boardServlet.execute(httpRequest, httpResponse);

		// then
		Assertions.assertThat(actual).isEqualTo("/board/write.html");
	}

	@Test
	@DisplayName("sid null인 경우")
	void nullSid() {
		// given
		HashMap<String, Object> model = new HashMap<>();
		model.put("title", "test title");
		model.put("content", "test content");
		model.put("writer", "test writer");
		Board board = BoardFactory.createBoard(model);
		BoardDatabase.save(board);

		HashMap<String, String> cookieMap = new HashMap<>();
		Cookies cookies = new Cookies(cookieMap);

		addUser();

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setCookies(cookies);
		httpRequest.setModel(model);

		HttpResponse httpResponse = new HttpResponse();

		BoardServlet boardServlet = new BoardServlet();

		// when
		String actual = boardServlet.execute(httpRequest, httpResponse);

		// then
		Assertions.assertThat(actual).isEqualTo("redirect:/user/login.html");
	}

	@Test
	@DisplayName("sid에 해당하는 session 정보 없는 경우")
	void noSessionInfo() {
		// given
		HashMap<String, Object> model = new HashMap<>();
		model.put("title", "test title");
		model.put("content", "test content");
		model.put("writer", "test writer");
		Board board = BoardFactory.createBoard(model);
		BoardDatabase.save(board);

		HashMap<String, String> cookieMap = new HashMap<>();
		cookieMap.put("sid", "SessionId");
		Cookies cookies = new Cookies(cookieMap);

		addUser();
		SessionStorage.setSession("invalidSessionId", "userId");

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setCookies(cookies);
		httpRequest.setModel(model);

		HttpResponse httpResponse = new HttpResponse();

		BoardServlet boardServlet = new BoardServlet();

		// when
		String actual = boardServlet.execute(httpRequest, httpResponse);

		// then
		Assertions.assertThat(actual).isEqualTo("redirect:/user/login.html");
	}

	@Test
	@DisplayName("sid에 해당하는 user 정보 없는 경우")
	void noUserInfo() {
		// given
		HashMap<String, Object> model = new HashMap<>();
		model.put("title", "test title");
		model.put("content", "test content");
		model.put("writer", "test writer");
		Board board = BoardFactory.createBoard(model);
		BoardDatabase.save(board);

		HashMap<String, String> cookieMap = new HashMap<>();
		cookieMap.put("sid", "SessionId");
		Cookies cookies = new Cookies(cookieMap);

		SessionStorage.setSession("SessionId", "userId");

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setCookies(cookies);
		httpRequest.setModel(model);

		HttpResponse httpResponse = new HttpResponse();

		BoardServlet boardServlet = new BoardServlet();

		// when
		String actual = boardServlet.execute(httpRequest, httpResponse);

		// then
		Assertions.assertThat(actual).isEqualTo("redirect:/user/login.html");
	}

	private static void addUser() {
		User user = User.builder()
			.userId("userId")
			.password("password")
			.name("name")
			.email("<EMAIL>")
			.build();
		UserDatabase.addUser(user);
	}
}
