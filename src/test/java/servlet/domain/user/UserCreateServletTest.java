package servlet.domain.user;

import java.util.HashMap;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import db.UserDatabase;
import servlet.domain.user.exception.AlreadyExistUserException;
import webserver.http.HttpRequest;

class UserCreateServletTest {

    @BeforeEach
    void init() {
        UserDatabase.flush();
    }

    @Test
    @DisplayName("회원가입 성공")
    void createUser() {
        // given
        HashMap<String, String> model = new HashMap<>();
        model.put("userId", "tester");
        model.put("password", "1234");
        model.put("name", "testName");
        model.put("email", "test@test.com");

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setModel(model);

        UserCreateServlet userCreateServlet = new UserCreateServlet();

        // when
        String result = userCreateServlet.execute(httpRequest);

        // then
        Assertions.assertThat(result).isEqualTo("redirect:/index.html");
    }

    @Test
    @DisplayName("이미 존재하는 유저아이디로 회원가입 요청")
    void createUserFailure() {
        // given
        HashMap<String, String> model = new HashMap<>();
        model.put("userId", "tester");
        model.put("password", "1234");
        model.put("name", "testName");
        model.put("email", "test@test.com");

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setModel(model);

        UserCreateServlet userCreateServlet = new UserCreateServlet();
        userCreateServlet.execute(httpRequest);

        // when then
        Assertions.assertThatThrownBy(() -> userCreateServlet.execute(httpRequest))
                .isInstanceOf(AlreadyExistUserException.class);
    }
}