package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            String url = getUrl(in);
            DataOutputStream dos = new DataOutputStream(out);

            byte[] body = getBytes(url);

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private byte[] getBytes(String url) throws IOException {
        if(isFileRequest(url)){
            return Files.readAllBytes(new File("src/main/resources/templates" + url).toPath());
        }

        return "Hello Softeer".getBytes();
    }

    private boolean isFileRequest(String url) {
        String[] splitUrl = url.split("[.]");
        if(splitUrl.length == 0) return false;

        String fileExtension = splitUrl[splitUrl.length - 1];
        System.out.println(fileExtension);

        return Extension.isProvidedExtension(fileExtension);
    }

    private static String getUrl(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String[] firstLine = bufferedReader.readLine().split(" ");
        String url = firstLine[1];
        return url;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public enum Extension {
        HTML("html");
        
        private final String value;

        private Extension(String value) {
            this.value = value;
        }

        static boolean isProvidedExtension(String extension) {
            Optional<String> findExtension = Arrays.stream(Extension.values())
                    .map(t -> t.value)
                    .filter(ext -> ext.equals(extension))
                    .findFirst();

            return findExtension.isPresent();
        }
    }
}
