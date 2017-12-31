package it.albertus.util.version;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

import it.albertus.util.IOUtils;

public class LatestReleaseChecker implements ILatestReleaseChecker {

	private String accept = "*/json";

	@Override
	public String check(final UrlSupplier urlSupplier, final Proxy proxy, final LatestReleaseCallback callback) throws IOException {
		final URL url = urlSupplier.get();
		final URLConnection connection;
		if (proxy == null) {
			connection = url.openConnection();
		}
		else {
			connection = url.openConnection(proxy);
		}
		connection.addRequestProperty("Accept", accept);
		connection.addRequestProperty("Accept-Encoding", "gzip");

		final String contentEncoding = connection.getHeaderField("Content-Encoding");
		if ("gzip".equalsIgnoreCase(contentEncoding)) {
			InputStream in = null;
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				in = new GZIPInputStream(connection.getInputStream());
				IOUtils.copy(in, out, 2048);
			}
			finally {
				IOUtils.closeQuietly(in);
			}
			return callback.parse(out.toByteArray());
		}
		else {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			IOUtils.copy(connection.getInputStream(), out, 2048);
			return callback.parse(out.toByteArray());
		}
	}

	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

}
