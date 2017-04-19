package it.albertus.httpserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;

import javax.activation.MimetypesFileTypeMap;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import it.albertus.util.IOUtils;

public class StaticResourceHandler extends AbstractHttpHandler {

	protected static final int BUFFER_SIZE = 4096;

	protected static final String DEFAULT_CACHE_CONTROL = "no-transform,public,max-age=86400,s-maxage=259200";

	private final String resourceName;
	private final String urlPath;
	private final Headers headers;

	public StaticResourceHandler(final String resourceName, final String urlPath, final Headers headers) {
		this.resourceName = resourceName;
		this.urlPath = urlPath;
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
			responseHeaders.add("Content-Type", guessContentType(resourceName));
		}
	}

	protected static String guessContentType(final String fileName) {
		final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
		final String contentType;
		if ("ico".equalsIgnoreCase(extension)) {
			contentType = "image/x-icon";
		}
		else if ("css".equalsIgnoreCase(extension)) {
			contentType = "text/css";
		}
		else if ("js".equalsIgnoreCase(extension)) {
			contentType = "application/javascript";
		}
		else if ("xml".equalsIgnoreCase(extension)) {
			contentType = "application/xml";
		}
		else if ("xhtml".equalsIgnoreCase(extension)) {
			contentType = "application/xhtml+xml";
		}
		else if ("pdf".equalsIgnoreCase(extension)) {
			contentType = "application/pdf";
		}
		else {
			contentType = new MimetypesFileTypeMap().getContentType(fileName);
		}
		return contentType;
	}

	@Override
	public String getPath() {
		return urlPath;
	}

	public String getResourceName() {
		return resourceName;
	}

	public Headers getHeaders() {
		return headers;
	}

}
