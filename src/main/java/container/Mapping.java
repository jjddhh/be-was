package container;

import webserver.http.Method;

import java.util.Objects;

public class Mapping {

    private final String url;
    private final Method method;

    public Mapping(String url, Method method) {
        this.url = url;
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mapping mapping = (Mapping) o;
        return Objects.equals(url, mapping.url) && method == mapping.method;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, method);
    }
}
