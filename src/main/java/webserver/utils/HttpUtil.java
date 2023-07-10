package webserver.utils;

import java.io.*;

public class HttpUtil {
    public static String getContent(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append(System.lineSeparator());

            if (line.isEmpty()) {
                break;
            }
        }
        return content.toString();
    }

    public static String getUrl(String content) throws IOException {
        String[] splitContent = content.split(" ");
        String url = splitContent[1];

        return url;
    }
}
