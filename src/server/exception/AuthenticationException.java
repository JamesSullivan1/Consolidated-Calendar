package server.exception;

import java.io.IOException;

/**
 * Exception occured during Authentication.
 *
 */
public class AuthenticationException extends IOException {
	private static final long serialVersionUID = 1L;

	public AuthenticationException(String message) {
		super(message);
	}
}
