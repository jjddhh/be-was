package servlet.domain.user.exception;

public class UserNotExistException extends RuntimeException {

	public static final UserNotExistException Exception = new UserNotExistException();

	private UserNotExistException() {
		super("존재하지 않는 유저입니다.");
	}
}
