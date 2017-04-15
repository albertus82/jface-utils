package it.albertus.httpserver;

import java.io.IOException;

public class HttpException extends IOException {

	private static final long serialVersionUID = -3054911937326730091L;

	private final int statusCode;

	public HttpException(final int statusCode) {
		this.statusCode = statusCode;
	}

	public HttpException(final int statusCode, final String message) {
		super(message);
		this.statusCode = statusCode;
	}

	public HttpException(final int statusCode, final Throwable cause) {
		super(cause);
		this.statusCode = statusCode;
	}

	public HttpException(final int statusCode, final String message, final Throwable cause) {
		super(message, cause);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

}
