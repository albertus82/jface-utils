package it.albertus.httpserver;

import java.io.File;
import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class FilesHandler extends AbstractHttpHandler {

	private String fileBasePath;
	private String cacheControl;
	private boolean attachment;

	public FilesHandler(final String fileBasePath, final String urlBasePath) {
		setFileBasePath(fileBasePath);
		setPath(urlBasePath);
	}

	public FilesHandler() {/* Default constructor */}

	@Override
	protected void doGet(final HttpExchange exchange) throws IOException {
		sendStaticFile(exchange, new File(fileBasePath + getPathInfo(exchange)), attachment, cacheControl);
	}

	public String getFileBasePath() {
		return fileBasePath;
	}

	public void setFileBasePath(final String fileBasePath) {
		this.fileBasePath = normalizeBasePath(fileBasePath);
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
		if (!resourceBasePath.endsWith("/")) {
			normalizedBasePath += Character.toString('/');
		}
		return normalizedBasePath;
	}

}
