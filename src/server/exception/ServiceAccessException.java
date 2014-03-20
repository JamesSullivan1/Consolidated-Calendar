package server.exception;

import java.io.IOException;

/**
 * Exception occurred when an API service could not be accessed.
 */
public class ServiceAccessException extends IOException {
	private static final long serialVersionUID = 1L;

	public ServiceAccessException(String message) {
		super(message);
	}
}
