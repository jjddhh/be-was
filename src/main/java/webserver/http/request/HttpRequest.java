package webserver.http.request;

import webserver.exception.InvalidRequestException;
import webserver.http.Method;
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
    private static final Integer KEY_VALUE_PAIR = 2;

    private String header;
    private String body;
    private Method method;
    private String contentType;
    private String pathParam;
    private String path;
    private String param;
    private Map<String, String> cookies;
    private Map<String, Object> model;

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

    public Map<String, Object> createModel() {
        if (method == Method.GET) {
            return parseToModel(param);
        }

        return parseToModel(body);
    }

    private Map<String, Object> parseToModel(String value) {
        if (isEmptyValue(value)) {
            return new HashMap<>();
        }

        String[] parsedParam = value.split(INTER_PARAM_SEPARATOR);
        Map<String, Object> queryPair = new HashMap<>();
        for (String pair : parsedParam) {
            String[] splitPair = pair.split(INTRA_PARAM_SEPARATOR);
            verifyParamPair(splitPair);
            queryPair.put(splitPair[0], splitPair[1]);
        }

        return queryPair;
    }

    private static void verifyParamPair(String[] splitPair) {
        if(splitPair.length < KEY_VALUE_PAIR) throw InvalidRequestException.Exception;
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

    public Map<String, Object> getModel() {
        return model;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }
}
