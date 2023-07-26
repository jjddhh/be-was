package webserver.http.response;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.user.User;
import webserver.RequestHandler;
import webserver.exception.InvalidRequestException;
import webserver.http.util.FileUtil;

public class HttpResponse {

	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
	public static final Integer STATUS_OK = 200;
	public static final Integer STATUS_REDIRECT = 303;
	public static final Integer STATUS_NOT_FOUND = 404;
	public static final Integer STATUS_METHOD_NOT_ALLOWED = 405;
	public static final Integer STATUS_INVALID_REQUEST = 450;

	private int status = 200;
	private byte[] body;
	private String contentType;
	private String redirectUrl;
	private Map<String, String> header = new HashMap<String, String>();

	public HttpResponse(int status, byte[] body, String contentType, String redirectUrl, Map<String, String> header) {
		this.status = status;
		this.body = body;
		this.contentType = contentType;
		this.redirectUrl = redirectUrl;
		this.header = header;
	}

	public HttpResponse() {}

	private static byte[] getResourceBytes(final String url) throws IOException {
		if (FileUtil.isStaticResourceRequest(url)) {
			String filePath = FileUtil.getFilePath(url);
			return Files.readAllBytes(new File(filePath).toPath());
		}

		throw InvalidRequestException.Exception;
	}

	public void doResponse(final DataOutputStream dos) throws IOException {

		if (status == STATUS_OK) {
			response200Header(dos);
		}

		if (status == STATUS_REDIRECT) {
			response303Header(dos, redirectUrl);
		}

		if (status == STATUS_INVALID_REQUEST) {
			response450Header(dos);
		}

		if (status == STATUS_NOT_FOUND) {
			response404Header(dos);
		}

		if (status == STATUS_METHOD_NOT_ALLOWED) {
			response405Header(dos);
		}

		responseBody(dos, body);
	}

	private void response200Header(DataOutputStream dos) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + body.length + "\r\n");

			setCookie(dos);

			dos.writeBytes("\r\n");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void response303Header(DataOutputStream dos, String redirectUrl) {
		try {
			dos.writeBytes("HTTP/1.1 303 See Other\r\n");
			dos.writeBytes("Location: " + redirectUrl + "\r\n");
			dos.writeBytes("Cache-Control: no-cache, no-store, must-revalidate\r\n");
			dos.writeBytes("Pragma: no-cache\r\n");
			dos.writeBytes("Expires: 0\r\n");

			setCookie(dos);

			dos.writeBytes("\r\n");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void response450Header(DataOutputStream dos) {
		try {
			dos.writeBytes("HTTP/1.1 450 INVALID_REQUEST \r\n");
			dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + body.length + "\r\n");

			dos.writeBytes("\r\n");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void response404Header(DataOutputStream dos) {
		try {
			dos.writeBytes("HTTP/1.1 404 NOT_FOUND \r\n");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void response405Header(DataOutputStream dos) {
		try {
			dos.writeBytes("HTTP/1.1 405 METHOD_NOT_ALLOWED \r\n");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void setCookie(DataOutputStream dos) throws IOException {
		String cookie = header.get("Cookie");
		if (Objects.nonNull(cookie)) {
			dos.writeBytes("Set-Cookie: sid=" + cookie + "; Path=/ \r\n");
		}
	}

	private void responseBody(final DataOutputStream dos, final byte[] body) {
		try {
			if(Objects.nonNull(body)) {
				dos.write(body, 0, body.length);
			}
			dos.flush();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public void setRedirectResponse(String redirectUrl) {
		this.status = STATUS_REDIRECT;
        this.redirectUrl = redirectUrl;
	}

	public void setDefaultResponse(String body, String contentType) {
		this.status = STATUS_OK;
        this.body = body.getBytes();
        this.contentType = contentType;
	}

	public void setResourceResponse(String resourcePath, String contentType) throws IOException {
		try {
			this.status = STATUS_OK;
			this.body = getResourceBytes(resourcePath);
			this.contentType = contentType;
		} catch (InvalidRequestException exception) {
			this.status = STATUS_NOT_FOUND;
		}
	}

	public void setBadRequestResponse() throws IOException {
		this.status = STATUS_INVALID_REQUEST;
		this.body = getResourceBytes("/error.html");
		this.contentType = "text/html";
	}

	public Map<String, String> getHeader() {
		return header;
	}
}
