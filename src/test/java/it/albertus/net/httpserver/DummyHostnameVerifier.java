package it.albertus.net.httpserver;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class DummyHostnameVerifier implements HostnameVerifier {

	private final String hostname;

	public DummyHostnameVerifier(final String hostname) {
		this.hostname = hostname;
	}

	@Override
	public boolean verify(final String hostname, final SSLSession session) {
		return hostname.equals(this.hostname);
	}

}
