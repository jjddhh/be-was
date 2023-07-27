package servlet.domain.base;

import container.annotation.MyMapping;
import container.annotation.ResponseBody;
import servlet.Servlet;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

@MyMapping(url = "/")
@ResponseBody
public class HomeServlet implements Servlet {

	@Override
	public String execute(HttpRequest httpRequest, HttpResponse httpResponse) {
		return "redirect:/index.html";
	}
}
