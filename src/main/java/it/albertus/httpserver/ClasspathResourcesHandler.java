package it.albertus.httpserver;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class ClasspathResourcesHandler extends AbstractHttpHandler {

	private String resourceBasePath;
	private String cacheControl;

	public ClasspathResourcesHandler(final String resourceBasePath, final String urlBasePath, final String cacheControl) {
		this.resourceBasePath = resourceBasePath;
		this.cacheControl = cacheControl;
		setPath(urlBasePath);
	}

	public ClasspathResourcesHandler() {/* Default constructor */}

	@Override
	protected void doGet(final HttpExchange exchange) throws IOException {
		sendStaticResource(exchange, resourceBasePath + getPathInfo(exchange), cacheControl);
	}

	public String getResourceBasePath() {
		return resourceBasePath;
	}

	public void setResourceBasePath(final String resourceBasePath) {
		this.resourceBasePath = resourceBasePath;
	}

	public String getCacheControl() {
		return cacheControl;
	}

	public void setCacheControl(final String cacheControl) {
		this.cacheControl = cacheControl;
	}

}
