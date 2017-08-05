package it.albertus.httpserver;

import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public interface HttpPathHandler extends HttpHandler {

	String getPath();

}
