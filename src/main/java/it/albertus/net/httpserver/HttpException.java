package it.albertus.net.httpserver;

public class HttpException extends RuntimeException {

	private static final long serialVersionUID = -7213287704920911183L;

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
