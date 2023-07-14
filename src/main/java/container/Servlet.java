package container;

import java.util.Map;

public interface Servlet {

	void execute(Map<String, String> model);
}
