package server.exception;

import java.io.IOException;

/**
 * Exception occurred when attempting to download an ICS file's data.
 *
 */
public class ICSParseException extends IOException {
	private static final long serialVersionUID = 1L;

	public ICSParseException(String message) {
		super(message);
	}
}
