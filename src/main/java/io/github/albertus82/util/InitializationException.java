package io.github.albertus82.util;

public class InitializationException extends RuntimeException {

	private static final long serialVersionUID = 7424607632683923770L;

	public InitializationException() {}

	public InitializationException(final String message) {
		super(message);
	}

	public InitializationException(final Throwable cause) {
		super(cause);
	}

	public InitializationException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
