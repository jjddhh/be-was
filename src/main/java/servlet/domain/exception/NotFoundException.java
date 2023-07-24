package servlet.domain.exception;

import servlet.domain.user.exception.AlreadyExistUserException;

public class NotFoundException extends RuntimeException {

	public static final NotFoundException Exception = new NotFoundException();

	private NotFoundException() {
		super("잘못된 요청입니다.");
	}
}
