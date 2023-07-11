package webserver.utils.view;

enum Extension {

    HTML("html");

    private final String value;

    private Extension(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}