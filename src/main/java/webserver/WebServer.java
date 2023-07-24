package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import db.BoardDatabase;
import db.UserDatabase;
import model.board.Board;
import model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import container.MyContainer;

public class WebServer {

	private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
	private static final int DEFAULT_PORT = 8080;
	private static final int MAX_THREADS = 100;

	public static void main(String args[]) throws Exception {
		int port = 0;
		if (args == null || args.length == 0) {
			port = DEFAULT_PORT;
		} else {
			port = Integer.parseInt(args[0]);
		}

		MyContainer.start(WebServer.class);

		User user = User.builder()
				.userId("test")
				.password("1234")
				.name("testName")
				.email("test@test")
				.build();
		UserDatabase.addUser(user);

		Board board = Board.builder()
				.createdAt(LocalDate.now())
				.title("제목입니다")
				.contents("내용")
				.writer("me")
				.build();
		BoardDatabase.save(board);


		ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);


		// 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
		try (ServerSocket listenSocket = new ServerSocket(port)) {
			logger.info("Web Application Server started {} port.", port);

			// 클라이언트가 연결될때까지 대기한다.
			Socket connection;
			while ((connection = listenSocket.accept()) != null) {
				Runnable worker = new RequestHandler(connection);
				executor.execute(worker);
			}
		}

		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		logger.info("Server closed...");
	}
}
