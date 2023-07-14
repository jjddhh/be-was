package servlet.domain.user.exception;

public class AlreadyExistUserException extends RuntimeException {

	public static final AlreadyExistUserException Exception = new AlreadyExistUserException();

	private AlreadyExistUserException() {
		super("잘못된 요청입니다.");
	}
}
