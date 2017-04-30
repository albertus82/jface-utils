package it.albertus.httpserver;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class DummyTrustManager implements X509TrustManager {

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}

	@Override
	public void checkClientTrusted(final X509Certificate[] certs, final String authType) {/* Ignore */}

	@Override
	public void checkServerTrusted(final X509Certificate[] certs, final String authType) {/* Ignore */}

}
