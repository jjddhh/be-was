package webserver.exception;

public class InvalidRequestException extends RuntimeException {
    public static final InvalidRequestException Exception = new InvalidRequestException();

    private InvalidRequestException() {
        super("잘못된 요청입니다.");
    }
}
