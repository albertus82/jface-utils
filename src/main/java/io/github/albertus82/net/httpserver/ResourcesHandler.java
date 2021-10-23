package io.github.albertus82.net.httpserver;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import io.github.albertus82.net.httpserver.config.IHttpServerConfig;

@SuppressWarnings("restriction")
public class ResourcesHandler extends AbstractStaticHandler {

	public ResourcesHandler(final IHttpServerConfig config, final String resourceBasePath, final String urlBasePath) {
		this(config);
		setBasePath(resourceBasePath);
		setPath(urlBasePath);
	}

	public ResourcesHandler(final IHttpServerConfig config, final Package resourceBasePackage, final String urlBasePath) {
		this(config, resourceBasePackage != null ? resourceBasePackage.getName().replace('.', '/') : "/", urlBasePath);
	}

	public ResourcesHandler(final IHttpServerConfig config) {
		super(config);
	}

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
