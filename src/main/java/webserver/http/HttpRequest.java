package webserver.http;

import webserver.exception.InvalidRequestException;
import webserver.http.util.HttpUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpRequest {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String INTER_PARAM_SEPARATOR = "[&]";
    private static final String INTRA_PARAM_SEPARATOR = "[=]";

    private String header;
    private String body;
    private Method method;
    private String contentType;
    private String pathParam;
    private String path;
    private String param;
    private Map<String, String> cookies;
    private Map<String, String> model;

    public HttpRequest() {
    }

    public HttpRequest(final BufferedReader reader) throws IOException {
        this(HttpUtil.getContent(reader));
    }

    public HttpRequest(final String content) throws IOException {
        this.header = HttpUtil.extractHeader(content);
        this.body = HttpUtil.extractBody(content);
        this.method = HttpUtil.getMethod(header);
        this.contentType = HttpUtil.getContentType(header);
        this.pathParam = HttpUtil.getPathParam(header);
        this.cookies = HttpUtil.getCookies(header);
        this.path = HttpUtil.getPath(pathParam);
        this.param = HttpUtil.getParam(pathParam);
        this.model = createModel();
    }

    public Map<String, String> createModel() {
        if (method == Method.GET) {
            return parseToModel(param);
        }

        return parseToModel(body);
    }

    private Map<String, String> parseToModel(String value) {
        if (isEmptyValue(value)) {
            return new HashMap<>();
        }

        String[] parsedParam = value.split(INTER_PARAM_SEPARATOR);
        Map<String, String> queryPair = new HashMap<>();
        for (String pair : parsedParam) {
            String[] splitPair = pair.split(INTRA_PARAM_SEPARATOR);
            if(splitPair.length < 2) throw InvalidRequestException.Exception;
            queryPair.put(splitPair[0], splitPair[1]);
        }

        return queryPair;
    }

    private boolean isEmptyValue(String value) {
        return Objects.isNull(value) || value.isEmpty();
    }

    public Method getMethod() {
        return method;
    }

    public String getContentType() {
        return contentType;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getModel() {
        return model;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setModel(Map<String, String> model) {
        this.model = model;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }
}
