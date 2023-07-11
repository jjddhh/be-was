package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.Database;
import model.User;
import model.factory.UserFactory;
import webserver.utils.HttpUtil;
import webserver.utils.view.FileUtil;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private final String USER_REGISTER_URL = "/user/create";

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
             BufferedOutputStream bufferedOut = new BufferedOutputStream(connection.getOutputStream());
             DataOutputStream dos = new DataOutputStream(bufferedOut)) {

            final String content = HttpUtil.getContent(reader);
            final String url = HttpUtil.getUrl(content);

            dispatchRequest(url);

            byte[] body = getBytes(url);

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void dispatchRequest(final String url) {
        String[] splitUrl = url.split("[?]");
        if(splitUrl[0].equals(USER_REGISTER_URL)) {

            if(splitUrl.length > 1) {
                String[] query = splitUrl[1].split("[&]");

                Map<String, String> queryPair = new HashMap<>();
                for (String pair : query) {
                    String[] splitPair = pair.split("[=]");
                    queryPair.put(splitPair[0], splitPair[1]);
                }

                User user = UserFactory.createUser(queryPair);

                Database.addUser(user);

                System.out.println(Database.findUserById(user.getUserId()));
            }
        }
    }

    private byte[] getBytes(final String url) throws IOException {
        if(FileUtil.isFileRequest(url)){
            return Files.readAllBytes(new File("src/main/resources/templates" + url).toPath());
        }

        return "Hello Softeer".getBytes();
    }

    private void response200Header(final DataOutputStream dos, final int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(final DataOutputStream dos, final byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
