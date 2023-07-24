package servlet.domain.html;

import container.annotation.MyMapping;
import container.annotation.ResponseBody;
import servlet.Servlet;
import webserver.http.HttpRequest;

@MyMapping(url = "/")
@ResponseBody
public class HomeServlet implements Servlet {

	@Override
	public String execute(HttpRequest httpRequest) {
		return "redirect:/index.html";
	}
}
