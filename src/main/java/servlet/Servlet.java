package servlet;

import webserver.http.HttpRequest;

import java.util.Map;

public interface Servlet {

	String execute(HttpRequest httpRequest);
}
