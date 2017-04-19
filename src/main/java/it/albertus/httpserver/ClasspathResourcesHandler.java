package it.albertus.httpserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map.Entry;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import it.albertus.util.IOUtils;
import it.albertus.util.StringUtils;

public class ClasspathResourcesHandler extends AbstractHttpHandler {

	protected static final String DEFAULT_CACHE_CONTROL = "no-transform,public,max-age=86400,s-maxage=259200";

	private final String resourceBasePath;
	private final Headers headers;

	public ClasspathResourcesHandler(final String resourceBasePath, final String urlBasePath, final Headers headers) {
		this.resourceBasePath = resourceBasePath;
		setPath(urlBasePath);
		this.headers = headers;
	}

	public ClasspathResourcesHandler(final String resourceBasePath, final String urlBasePath) {
		this(resourceBasePath, urlBasePath, null);
	}

	public ClasspathResourcesHandler(final String resourceBasePath, final Headers headers) {
		this(resourceBasePath, resourceBasePath, headers);
	}

	public ClasspathResourcesHandler(final String resourceBasePath) {
		this(resourceBasePath, resourceBasePath, null);
	}

	@Override
	protected void doGet(final HttpExchange exchange) throws IOException { // FIXME avoid ByteArrayOutputStream
		final String pathInfo = StringUtils.substringAfter(exchange.getRequestURI().toString(), getPath());
		InputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;
		try {
			inputStream = getClass().getResourceAsStream(resourceBasePath + pathInfo);
			if (inputStream == null) {
				notFound(exchange);
				return;
			}
			outputStream = new ByteArrayOutputStream();
			IOUtils.copy(inputStream, outputStream, BUFFER_SIZE);
		}
		finally {
			IOUtils.closeQuietly(outputStream, inputStream);
		}
		sendResponse(exchange, outputStream.toByteArray());
	}

	private void notFound(final HttpExchange exchange) throws IOException {
		sendResponse(exchange, null, HttpURLConnection.HTTP_NOT_FOUND);
	}

	@Override
	protected void addCommonHeaders(final HttpExchange exchange) {
		final Headers responseHeaders = exchange.getResponseHeaders();
		if (this.headers != null) {
			for (final Entry<String, List<String>> entry : this.headers.entrySet()) {
				responseHeaders.put(entry.getKey(), entry.getValue());
			}
		}
		super.addCommonHeaders(exchange);
		if (!responseHeaders.containsKey("Cache-Control")) {
			responseHeaders.add("Cache-Control", DEFAULT_CACHE_CONTROL);
		}
	}

	@Override
	protected void addContentTypeHeader(final HttpExchange exchange) {
		final Headers responseHeaders = exchange.getResponseHeaders();
		if (!responseHeaders.containsKey("Content-Type")) {
			super.addContentTypeHeader(exchange);
		}
	}

	protected String getResourceBasePath() {
		return resourceBasePath;
	}

	protected Headers getHeaders() {
		return headers;
	}

}
