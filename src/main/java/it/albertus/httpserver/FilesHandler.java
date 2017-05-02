package it.albertus.httpserver;

import java.io.File;
import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class FilesHandler extends AbstractStaticHandler {

	public FilesHandler(final String fileBasePath, final String urlBasePath) {
		setBasePath(fileBasePath);
		setPath(urlBasePath);
	}

	public FilesHandler() {/* Default constructor */}

	@Override
	protected void doGet(final HttpExchange exchange) throws IOException {
		sendStaticFile(exchange, new File(getBasePath() + getPathInfo(exchange)), isAttachment(), getCacheControl());
	}

	@Override
	protected String normalizeBasePath(final String resourceBasePath) {
		String normalizedBasePath = resourceBasePath;
		if (!resourceBasePath.endsWith("/")) {
			normalizedBasePath += Character.toString('/');
		}
		return normalizedBasePath;
	}

}
