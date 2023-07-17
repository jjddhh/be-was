package webserver.http;

import webserver.http.util.HttpUtil;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {

    private final String contentType;
    private final String pathParam;
    private final String path;
    private final String param;

    public HttpRequest(final BufferedReader reader) throws IOException {
        final String header = HttpUtil.getContent(reader);
        this.contentType = HttpUtil.getContentType(header);
        this.pathParam = HttpUtil.getPathParam(header);
        this.path = HttpUtil.getPath(pathParam);
        this.param = HttpUtil.getParam(pathParam);
    }

    public String getContentType() {
        return contentType;
    }

    public String getPath() {
        return path;
    }

    public String getParam() {
        return param;
    }
}
