package webserver;

import java.io.*;
import java.lang.annotation.Annotation;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import container.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import container.MyContainer;
import container.Servlet;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

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

			HttpRequest httpRequest = new HttpRequest(reader);

			HttpResponse httpResponse = dispatchRequest(httpRequest);

			httpResponse.doResponse(dos);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private HttpResponse dispatchRequest(HttpRequest httpRequest) throws IOException {
		Object mappingClass = MyContainer.getMappingClass(httpRequest.getPath());

		if (mappingClass instanceof Servlet) {
			return processServlet((Servlet) mappingClass, httpRequest);
		}

		return HttpResponse.createResourceResponse(httpRequest.getPath(), httpRequest.getContentType(), httpRequest.getModel());
	}

	private HttpResponse processServlet(Servlet servlet, HttpRequest httpRequest) throws IOException {
		Annotation[] declaredAnnotations = servlet.getClass().getDeclaredAnnotations();
		Map<String, String> model = httpRequest.getModel();

		String result = servlet.execute(model);

		if (isResponseBody(declaredAnnotations)) {
			return HttpResponse.createDefaultResponse(result, httpRequest.getContentType(), model);
		}

		if (isRedirect(result)) {
			return HttpResponse.createRedirectResponse(result, model);
		}

		return HttpResponse.createResourceResponse(result, httpRequest.getContentType(), model);
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