package servlet;

import webserver.http.HttpRequest;

public interface Servlet {

	String execute(HttpRequest httpRequest);
}
