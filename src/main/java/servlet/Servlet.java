package servlet;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public interface Servlet {

	String execute(HttpRequest httpRequest, HttpResponse httpResponse);
}
