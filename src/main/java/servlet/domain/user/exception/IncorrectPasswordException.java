package servlet.domain.user.exception;

public class IncorrectPasswordException extends RuntimeException {

	public static final IncorrectPasswordException Exception = new IncorrectPasswordException();

	private IncorrectPasswordException() {
		super("잘못된 비밀번호입니다.");
	}
}
