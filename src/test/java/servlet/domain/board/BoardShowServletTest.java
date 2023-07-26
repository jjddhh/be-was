package servlet.domain.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;

import db.BoardDatabase;
import db.UserDatabase;
import model.user.User;
import session.SessionStorage;
import webserver.http.request.Cookies;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

class BoardShowServletTest {

    @BeforeEach
    void init() {
        UserDatabase.flush();
        BoardDatabase.flush();
        SessionStorage.flush();
    }

    // @Test
    @DisplayName("게시판글 상세보기 성공")
    void execute() {
        // given
        HashMap<String, Object> model = new HashMap<>();
        model.put("id", "1");

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
        httpRequest.setModel(model);
        httpRequest.setCookies(cookies);

        HttpResponse httpResponse = new HttpResponse();

        BoardShowServlet boardShowServlet = new BoardShowServlet();

        // when
        boardShowServlet.execute(httpRequest, httpResponse);

        // then
    }
}
