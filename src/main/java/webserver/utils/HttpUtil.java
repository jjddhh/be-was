package webserver.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import webserver.exception.InvalidRequestException;

import java.io.*;

public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public static String getContent(BufferedReader reader) throws IOException {
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append(System.lineSeparator());

            if (line.isEmpty()) {
                break;
            }

            logger.debug(line);
        }

        verifyContent(content);

        return content.toString();
    }

    private static void verifyContent(StringBuilder content) {
        if(content.toString().isEmpty()) throw InvalidRequestException.Exception;
    }

    public static String getUrl(String content) throws IOException {
        String[] splitContent = content.split(" ");
        String url = splitContent[1];

        return url;
    }
}
