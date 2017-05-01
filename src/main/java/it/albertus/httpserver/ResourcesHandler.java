package it.albertus.httpserver;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class ResourcesHandler extends AbstractHttpHandler {

	private String resourceBasePath;
	private String cacheControl;
	private boolean attachment;

	public ResourcesHandler(final String resourceBasePath, final String urlBasePath) {
		setResourceBasePath(resourceBasePath);
		setPath(urlBasePath);
	}

	public ResourcesHandler(final Package resourceBasePackage, final String urlBasePath) {
		this(resourceBasePackage.getName().replace('.', '/'), urlBasePath);
	}

	public ResourcesHandler() {/* Default constructor */}

	@Override
	protected void doGet(final HttpExchange exchange) throws IOException {
		sendStaticResource(exchange, resourceBasePath + getPathInfo(exchange), attachment, cacheControl);
	}

	public String getResourceBasePath() {
		return resourceBasePath;
	}

	public void setResourceBasePath(final String resourceBasePath) {
		this.resourceBasePath = normalizeBasePath(resourceBasePath);
	}

	public String getCacheControl() {
		return cacheControl;
	}

	public void setCacheControl(final String cacheControl) {
		this.cacheControl = cacheControl;
	}

	public boolean isAttachment() {
		return attachment;
	}

	public void setAttachment(final boolean attachment) {
		this.attachment = attachment;
	}

	private static String normalizeBasePath(final String resourceBasePath) {
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
