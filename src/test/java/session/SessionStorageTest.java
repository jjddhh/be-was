package session;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SessionStorageTest {

    @BeforeEach
    void init() {
        SessionStorage.flush();
    }

    @Test
    @DisplayName("세션정보 저장되는지 확인")
    void setSession() {
        //given
        String sessionId = "sessionId";
        String userId = "userId";

        //when
        SessionStorage.setSession(sessionId, userId);

        //then
        Assertions.assertThat(SessionStorage.getSessionUserId(sessionId).isPresent()).isEqualTo(true);
    }
}