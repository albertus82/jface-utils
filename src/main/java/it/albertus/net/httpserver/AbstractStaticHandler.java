package it.albertus.net.httpserver;

import it.albertus.net.httpserver.config.IHttpServerConfig;

public abstract class AbstractStaticHandler extends BaseHttpHandler {

	private String basePath;
	private String cacheControl;
	private boolean attachment;

	protected AbstractStaticHandler(final IHttpServerConfig config) {
		super(config);
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(final String basePath) {
		this.basePath = normalizeBasePath(basePath);
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

	protected abstract String normalizeBasePath(String basePath);

}
