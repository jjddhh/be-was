package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import container.MyContainer;
import container.Servlet;
import webserver.utils.HttpUtil;
import webserver.utils.view.FileUtil;

public class RequestHandler implements Runnable {

	private static final String STATIC_PATH = "src/main/resources/templates";
	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
			connection.getPort());

		try (InputStream in = connection.getInputStream();
			 BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
			 BufferedOutputStream bufferedOut = new BufferedOutputStream(connection.getOutputStream());
			 DataOutputStream dos = new DataOutputStream(bufferedOut)) {

			final String content = HttpUtil.getContent(reader);
			final String pathParam = HttpUtil.getPathParam(content);

			dispatchRequest(pathParam);

			byte[] body = getBytes(pathParam);

			response200Header(dos, body.length);
			responseBody(dos, body);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void dispatchRequest(final String pathParam) {
		String path = HttpUtil.getPath(pathParam);
		String param = HttpUtil.getParam(pathParam);

		Object mappingClass = MyContainer.getMappingClass(path);
		if (mappingClass instanceof Servlet) {
			Map<String, String> model = HttpUtil.getModel(param);

			((Servlet)mappingClass).execute(model);
		}
	}

	private byte[] getBytes(final String url) throws IOException {
		if (FileUtil.isFileRequest(url)) {
			return Files.readAllBytes(new File(STATIC_PATH + url).toPath());
		}

		return "Hello Softeer".getBytes();
	}

	private void response200Header(final DataOutputStream dos, final int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void responseBody(final DataOutputStream dos, final byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
}
