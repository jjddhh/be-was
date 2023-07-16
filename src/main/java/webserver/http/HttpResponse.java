package webserver.http;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.RequestHandler;

public class HttpResponse {

	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

	private final DataOutputStream dos;
	private final byte[] body;
	private final String contentType;

	public HttpResponse(final DataOutputStream dos, final byte[] body, final String contentType) {
		this.dos = dos;
		this.body = body;
		this.contentType = contentType;
	}

	public void doResponse() {
		response200Header();
		responseBody(dos, body);
	}

	private void response200Header() {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + body.length + "\r\n");
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
