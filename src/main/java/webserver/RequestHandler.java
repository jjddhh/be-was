package webserver;

import java.io.*;
import java.lang.annotation.Annotation;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import container.Mapping;
import container.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import container.MyContainer;
import servlet.Servlet;
import webserver.exception.InvalidRequestException;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

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

			HttpResponse httpResponse = new HttpResponse();
			try {
				HttpRequest httpRequest = new HttpRequest(reader);
				dispatchRequest(httpRequest, httpResponse);
			} catch (InvalidRequestException e) {
				httpResponse.setBadRequestResponse();
			}

			httpResponse.doResponse(dos);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void dispatchRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
		Mapping mapping = new Mapping(httpRequest.getPath(), httpRequest.getMethod());
		Object mappingClass = MyContainer.getMappingClass(mapping);

		if (mappingClass instanceof Servlet) {
			processServlet((Servlet) mappingClass, httpRequest, httpResponse);
			return;
		}

		httpResponse.setResourceResponse(httpRequest.getPath(), httpRequest.getContentType());
	}

	private void processServlet(Servlet servlet, HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
		String result = servlet.execute(httpRequest, httpResponse);

		if (isRedirect(result)) {
			String redirectUrl = result.split(":")[1];
			httpResponse.setRedirectResponse(redirectUrl);
			return;
		}

		Annotation[] declaredAnnotations = servlet.getClass().getDeclaredAnnotations();
		if (isResponseBody(declaredAnnotations)) {
			String body = result;
			httpResponse.setDefaultResponse(body, httpRequest.getContentType());
			return;
		}

		String resourcePath = result;
		httpResponse.setResourceResponse(resourcePath, httpRequest.getContentType());
	}

	private static boolean isResponseBody(Annotation[] declaredAnnotations) {
		return Arrays.stream(declaredAnnotations)
				.filter(annotation -> annotation instanceof ResponseBody)
				.findAny()
				.isPresent();
	}

	private static boolean isRedirect(String result) {
		return result.startsWith("redirect:");
	}
}
