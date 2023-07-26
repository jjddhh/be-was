package servlet.domain.user;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import db.UserDatabase;
import servlet.domain.user.exception.AlreadyExistUserException;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

class UserCreateServletTest {

    @BeforeEach
    void init() {
        UserDatabase.flush();
    }

    @Test
    @DisplayName("회원가입 성공")
    void createUser() {
        // given
        Map<String, Object> model = new HashMap<>();
        model.put("userId", "tester");
        model.put("password", "1234");
        model.put("name", "testName");
        model.put("email", "test@test.com");

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setModel(model);

        HttpResponse httpResponse = new HttpResponse();

        UserCreateServlet userCreateServlet = new UserCreateServlet();

        // when
        String result = userCreateServlet.execute(httpRequest, httpResponse);

        // then
        Assertions.assertThat(result).isEqualTo("redirect:/index.html");
    }

    @Test
    @DisplayName("이미 존재하는 유저아이디로 회원가입 요청")
    void createUserFailure() {
        // given
        Map<String, Object> model = new HashMap<>();
        model.put("userId", "tester");
        model.put("password", "1234");
        model.put("name", "testName");
        model.put("email", "test@test.com");

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setModel(model);

        HttpResponse httpResponse = new HttpResponse();

        UserCreateServlet userCreateServlet = new UserCreateServlet();
        userCreateServlet.execute(httpRequest, httpResponse);

        // when then
        Assertions.assertThatThrownBy(() -> userCreateServlet.execute(httpRequest, httpResponse))
                .isInstanceOf(AlreadyExistUserException.class);
    }
}
