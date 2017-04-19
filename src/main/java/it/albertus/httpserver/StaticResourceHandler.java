package it.albertus.httpserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import it.albertus.util.IOUtils;

public class StaticResourceHandler extends AbstractHttpHandler {

	protected static final String DEFAULT_CACHE_CONTROL = "no-transform,public,max-age=86400,s-maxage=259200";

	private final String resourceName;
	private final Headers headers;

	public StaticResourceHandler(final String resourceName, final String urlPath, final Headers headers) {
		this.resourceName = resourceName;
		setPath(urlPath);
		this.headers = headers;
	}

	public StaticResourceHandler(final String resourceName, final String urlPath) {
		this(resourceName, urlPath, null);
	}

	public StaticResourceHandler(final String resourceName, final Headers headers) {
		this(resourceName, resourceName, headers);
	}

	public StaticResourceHandler(final String resourceName) {
		this(resourceName, resourceName, null);
	}

	@Override
	protected void doGet(final HttpExchange exchange) throws IOException { // FIXME avoid ByteArrayOutputStream
		InputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;

		try {
			inputStream = getClass().getResourceAsStream(resourceName);
			outputStream = new ByteArrayOutputStream();
			IOUtils.copy(inputStream, outputStream, BUFFER_SIZE);
		}
		finally {
			IOUtils.closeQuietly(outputStream, inputStream);
		}

		sendResponse(exchange, outputStream.toByteArray());
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

	public String getResourceName() {
		return resourceName;
	}

	public Headers getHeaders() {
		return headers;
	}

}
