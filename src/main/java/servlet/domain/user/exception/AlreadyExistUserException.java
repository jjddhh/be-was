package servlet.domain.user.exception;

public class AlreadyExistUserException extends RuntimeException {

	public static final AlreadyExistUserException Exception = new AlreadyExistUserException();

	private AlreadyExistUserException() {
		super("동일한 유저가 이미 등록되어있습니다.");
	}
}