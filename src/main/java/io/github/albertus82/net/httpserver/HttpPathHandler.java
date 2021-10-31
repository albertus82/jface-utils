package io.github.albertus82.net.httpserver;

import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public interface HttpPathHandler extends HttpHandler {

	/**
	 * Returns the path associated with this handler, e.g., {@code /},
	 * {@code /home}, {@code /index}.
	 * 
	 * @return the path associated with this handler.
	 */
	String getPath();

}
