package it.albertus.httpserver;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class ClasspathResourcesHandler extends AbstractHttpHandler {

	private String resourceBasePath;

	public ClasspathResourcesHandler(final String resourceBasePath, final String urlBasePath) {
		this.resourceBasePath = resourceBasePath;
		setPath(urlBasePath);
	}

	public ClasspathResourcesHandler() {/* Default constructor */}

	@Override
	protected void doGet(final HttpExchange exchange) throws IOException {
		sendStaticResource(exchange, resourceBasePath);
	}

	public String getResourceBasePath() {
		return resourceBasePath;
	}

	public void setResourceBasePath(final String resourceBasePath) {
		this.resourceBasePath = resourceBasePath;
	}

}
