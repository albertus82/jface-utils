package it.albertus.httpserver;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings("restriction")
public class ResourcesHandler extends AbstractStaticHandler {

	public ResourcesHandler(final String resourceBasePath, final String urlBasePath) {
		setBasePath(resourceBasePath);
		setPath(urlBasePath);
	}

	public ResourcesHandler(final Package resourceBasePackage, final String urlBasePath) {
		this(resourceBasePackage != null ? resourceBasePackage.getName().replace('.', '/') : "/", urlBasePath);
	}

	public ResourcesHandler() {/* Default constructor */}

	@Override
	protected void doGet(final HttpExchange exchange) throws IOException {
		sendStaticResource(exchange, getBasePath() + getPathInfo(exchange), isAttachment(), getCacheControl());
	}

	@Override
	protected String normalizeBasePath(final String resourceBasePath) {
		String normalizedBasePath = resourceBasePath;
		if (!resourceBasePath.startsWith("/")) {
			normalizedBasePath = '/' + normalizedBasePath;
		}
		if (!resourceBasePath.endsWith("/")) {
			normalizedBasePath += Character.toString('/');
		}
		return normalizedBasePath;
	}

}
