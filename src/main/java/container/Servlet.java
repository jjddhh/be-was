package container;

import java.util.Map;

public interface Servlet {

	String execute(Map<String, String> model);
}
