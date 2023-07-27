package servlet.domain.user;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import db.UserDatabase;
import model.user.User;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

class UserLoginServletTest {

	@BeforeAll
	static void init() {
		User user = User.builder()
			.userId("tester")
			.password("1234")
			.name("testName")
			.email("test@test")
			.build();
		UserDatabase.addUser(user);
	}

	@Test
	@DisplayName("로그인 성공")
	void loginUser() {
	    // given
		Map<String, Object> model = new HashMap<>();
		model.put("userId", "tester");
		model.put("password", "1234");

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setModel(model);

		HttpResponse httpResponse = new HttpResponse();

		UserLoginServlet userLoginServlet = new UserLoginServlet();

		// when
		String result = userLoginServlet.execute(httpRequest, httpResponse);

		// then
		assertThat(result).isEqualTo("redirect:/index.html");
	}

	@Test
	@DisplayName("로그인시 패스워드 틀림")
	void wrongPassword() {
		// given
		Map<String, Object> model = new HashMap<>();
		model.put("userId", "tester");
		model.put("password", "12345");

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setModel(model);

		HttpResponse httpResponse = new HttpResponse();

		UserLoginServlet userLoginServlet = new UserLoginServlet();

		// when
		String result = userLoginServlet.execute(httpRequest, httpResponse);

		// then
		assertThat(result).isEqualTo("/user/login_failed.html");
	}

	@Test
	@DisplayName("로그인시 패스워드 틀림")
	void wrongUserId() {
		// given
		Map<String, Object> model = new HashMap<>();
		model.put("userId", "testee");
		model.put("password", "1234");

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setModel(model);

		HttpResponse httpResponse = new HttpResponse();

		UserLoginServlet userLoginServlet = new UserLoginServlet();

		// when
		String result = userLoginServlet.execute(httpRequest, httpResponse);

		// then
		assertThat(result).isEqualTo("/user/login_failed.html");
	}
}
