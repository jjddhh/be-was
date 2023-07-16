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
import webserver.http.HttpResponse;
import webserver.http.util.HttpUtil;
import webserver.http.util.FileUtil;

public class RequestHandler implements Runnable {

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

			final String header = HttpUtil.getContent(reader);
			final String pathParam = HttpUtil.getPathParam(header);
			final String contentType = HttpUtil.getContentType(header);

			dispatchRequest(pathParam);

			byte[] body = getBytes(pathParam);

			HttpResponse httpResponse = new HttpResponse(dos, body, contentType);
			httpResponse.doResponse();
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
			String filePath = FileUtil.getFilePath(url);
			return Files.readAllBytes(new File(filePath).toPath());
		}

		return "Hello Softeer".getBytes();
	}
}
