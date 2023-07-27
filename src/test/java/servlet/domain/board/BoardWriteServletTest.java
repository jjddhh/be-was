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
import webserver.exception.InvalidRequestException;
import webserver.http.request.Cookies;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

class BoardWriteServletTest {

	@BeforeEach
	void init() {
		UserDatabase.flush();
		BoardDatabase.flush();
		SessionStorage.flush();
	}

	@Test
	@DisplayName("게시판 글쓰기")
	void execute() {
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


		User user = User.builder()
			.userId("userId")
			.password("password")
			.name("name")
			.email("<EMAIL>")
			.build();
		SessionStorage.setSession("SessionId", "userId");
		UserDatabase.addUser(user);

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setCookies(cookies);
		httpRequest.setModel(model);

		HttpResponse httpResponse = new HttpResponse();

		BoardWriteServlet boardWriteServlet = new BoardWriteServlet();

		// when
		String result = boardWriteServlet.execute(httpRequest, httpResponse);

		// then
		Assertions.assertThat(result).isEqualTo("redirect:/index.html");
	}

	@Test
	@DisplayName("Session 존재하지 않는 경우")
	void noSession() {
		// given
		HashMap<String, Object> model = new HashMap<>();
		model.put("title", "test title");
		model.put("content", "test content");
		model.put("writer", "test writer");
		Board board = BoardFactory.createBoard(model);
		BoardDatabase.save(board);

		HashMap<String, String> cookieMap = new HashMap<>();
		Cookies cookies = new Cookies(cookieMap);

		User user = User.builder()
			.userId("userId")
			.password("password")
			.name("name")
			.email("<EMAIL>")
			.build();
		UserDatabase.addUser(user);

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setCookies(cookies);
		httpRequest.setModel(model);

		HttpResponse httpResponse = new HttpResponse();

		BoardWriteServlet boardWriteServlet = new BoardWriteServlet();

		// when then
		Assertions.assertThatThrownBy(() -> {boardWriteServlet.execute(httpRequest, httpResponse);})
			.isInstanceOf(InvalidRequestException.class);
	}

	@Test
	@DisplayName("존재하지 않는 SessionId")
	void invalidSessionId() {
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

		User user = User.builder()
			.userId("userId")
			.password("password")
			.name("name")
			.email("<EMAIL>")
			.build();
		SessionStorage.setSession("invalidSessionId", "userId");
		UserDatabase.addUser(user);

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setCookies(cookies);
		httpRequest.setModel(model);

		HttpResponse httpResponse = new HttpResponse();

		BoardWriteServlet boardWriteServlet = new BoardWriteServlet();

		// when then
		Assertions.assertThatThrownBy(() -> {boardWriteServlet.execute(httpRequest, httpResponse);})
			.isInstanceOf(InvalidRequestException.class);
	}

	@Test
	@DisplayName("SessionId에 해당하는 유저가 존재하지 않는 경우")
	void noSessionUser() {
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

		User user = User.builder()
			.userId("userId")
			.password("password")
			.name("name")
			.email("<EMAIL>")
			.build();
		SessionStorage.setSession("SessionId", "invalidId");
		UserDatabase.addUser(user);

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setCookies(cookies);
		httpRequest.setModel(model);

		HttpResponse httpResponse = new HttpResponse();

		BoardWriteServlet boardWriteServlet = new BoardWriteServlet();

		// when then
		Assertions.assertThatThrownBy(() -> {boardWriteServlet.execute(httpRequest, httpResponse);})
			.isInstanceOf(InvalidRequestException.class);
	}
}
