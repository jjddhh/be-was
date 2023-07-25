package servlet.domain.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import db.BoardDatabase;
import db.UserDatabase;
import model.user.User;
import session.SessionStorage;
import webserver.http.HttpRequest;

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
        HashMap<String, String> model = new HashMap<>();
        model.put("id", "1");

        HashMap<String, String> cookies = new HashMap<>();
        cookies.put("sid", "SessionId");

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

        BoardShowServlet boardShowServlet = new BoardShowServlet();

        // when
        boardShowServlet.execute(httpRequest);

        // then
    }
}