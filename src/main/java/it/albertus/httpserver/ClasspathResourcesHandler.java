package it.albertus.httpserver;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class ClasspathResourcesHandler extends AbstractHttpHandler {

	protected static final String DEFAULT_CACHE_CONTROL = "no-transform,public,max-age=86400,s-maxage=259200";

	private String resourceBasePath;
	private Headers headers;

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

	public ClasspathResourcesHandler() {
		this(null, null, null);
	}

	@Override
	protected void doGet(final HttpExchange exchange) throws IOException { // FIXME avoid ByteArrayOutputStream
		sendStaticResource(exchange, resourceBasePath);
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

	public String getResourceBasePath() {
		return resourceBasePath;
	}

	public void setResourceBasePath(final String resourceBasePath) {
		this.resourceBasePath = resourceBasePath;
	}

	public Headers getHeaders() {
		return headers;
	}

	public void setHeaders(final Headers headers) {
		this.headers = headers;
	}

}
