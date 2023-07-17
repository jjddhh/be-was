package webserver.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.RequestHandler;
import webserver.exception.InvalidRequestException;
import webserver.http.util.FileUtil;

public class HttpResponse {

	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

	private final int status;
	private final DataOutputStream dos;
	private byte[] body;
	private String contentType;
	private String redirectUrl;

	public HttpResponse(final int status, final DataOutputStream dos, final byte[] body, final String contentType) {
		this.status = status;
		this.dos = dos;
		this.body = body;
		this.contentType = contentType;
	}

	public HttpResponse(final int status, final DataOutputStream dos, final String redirectUrl) {
		this.status = status;
		this.dos = dos;
		this.redirectUrl = redirectUrl;
	}

	public static HttpResponse createRedirectResponse(DataOutputStream dos, String resourceUrl) {
		String redirectUrl = resourceUrl.split(":")[1];
		return new HttpResponse(301, dos, redirectUrl);
	}

	public static HttpResponse createDefaultResponse(DataOutputStream dos, String body, String contentType) {
		return new HttpResponse(200, dos, body.getBytes(), contentType);
	}

	public static HttpResponse createResourceResponse(DataOutputStream dos, String path, String contentType) throws
		IOException {
		byte[] body = getResourceBytes(path);
		return new HttpResponse(200, dos, body, contentType);
	}

	private static byte[] getResourceBytes(final String url) throws IOException {
		if (FileUtil.isFileRequest(url)) {
			String filePath = FileUtil.getFilePath(url);
			return Files.readAllBytes(new File(filePath).toPath());
		}

		throw InvalidRequestException.Exception;
	}

	public void doResponse() {
		switch (status) {
			case 200 :
				response200Header();
				break;
			case 301 :
				response301Header(redirectUrl);
				break;
		}

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

	private void response301Header(String redirectUrl) {
		try {
			dos.writeBytes("HTTP/1.1 301 Moved Temporarily\r\n");
			dos.writeBytes("Location: " + redirectUrl + "\r\n");
			dos.writeBytes("Cache-Control: no-cache, no-store, must-revalidate\r\n");
			dos.writeBytes("Pragma: no-cache\r\n");
			dos.writeBytes("Expires: 0\r\n");
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
