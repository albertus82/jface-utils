package io.github.albertus82.net.httpserver;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import io.github.albertus82.net.httpserver.config.IHttpServerConfig;

@SuppressWarnings("restriction")
public class FilesHandler extends AbstractStaticHandler {

	public FilesHandler(final IHttpServerConfig config, final String fileBasePath, final String urlBasePath) {
		this(config);
		setBasePath(fileBasePath);
		setPath(urlBasePath);
	}

	public FilesHandler(final IHttpServerConfig config) {
		super(config);
	}

	@Override
	protected void doGet(final HttpExchange exchange) throws IOException {
		sendStaticFile(exchange, getBasePath(), getPathInfo(exchange), isAttachment(), getCacheControl());
	}

	@Override
	protected String normalizeBasePath(final String fileBasePath) {
		String normalizedBasePath = fileBasePath;
		if (!fileBasePath.endsWith("/")) {
			normalizedBasePath += Character.toString('/');
		}
		return normalizedBasePath;
	}

}
