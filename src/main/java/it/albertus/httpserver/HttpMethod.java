package it.albertus.httpserver;

public class HttpMethod {

	public static final String OPTIONS = "options";
	public static final String GET = "get";
	public static final String HEAD = "head";
	public static final String POST = "post";
	public static final String PUT = "put";
	public static final String DELETE = "delete";
	public static final String TRACE = "trace";
	public static final String CONNECT = "connect";
	public static final String PATCH = "patch";

	private HttpMethod() {
		throw new IllegalAccessError("Constants class");
	}

}
