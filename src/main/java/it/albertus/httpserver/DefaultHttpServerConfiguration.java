package it.albertus.httpserver;

import java.util.logging.Level;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

public abstract class DefaultHttpServerConfiguration implements IHttpServerConfiguration {

	public static class Defaults {
		public static final int PORT = 8080;
		public static final boolean ENABLED = false;
		public static final boolean AUTHENTICATION = true;
		public static final String PASSWORD_HASH_ALGORITHM = "SHA-256";
		public static final int MAX_THREAD_COUNT = 16;
		public static final boolean SSL_ENABLED = false;
		public static final String SSL_KEYSTORE_TYPE = "JKS";
		public static final String SSL_PROTOCOL = "TLS";
		public static final String SSL_KMF_ALGORITHM = KeyManagerFactory.getDefaultAlgorithm();
		public static final String SSL_TMF_ALGORITHM = TrustManagerFactory.getDefaultAlgorithm();
		public static final long MAX_REQ_TIME = 60; // seconds
		public static final long MAX_RSP_TIME = 60 * 60 * 24L; // seconds
		public static final Level REQUEST_LOGGING_LEVEL = Level.INFO;
		public static final boolean COMPRESSION_ENABLED = true;

		private Defaults() {
			throw new IllegalAccessError("Constants class");
		}
	}

	@Override
	public boolean isEnabled() {
		return Defaults.ENABLED;
	}

	@Override
	public boolean isAuthenticationRequired() {
		return Defaults.AUTHENTICATION;
	}

	@Override
	public String getPasswordHashAlgorithm() {
		return Defaults.PASSWORD_HASH_ALGORITHM;
	}

	@Override
	public int getPort() {
		return Defaults.PORT;
	}

	@Override
	public long getMaxReqTime() {
		return Defaults.MAX_REQ_TIME;
	}

	@Override
	public long getMaxRspTime() {
		return Defaults.MAX_RSP_TIME;
	}

	@Override
	public boolean isSslEnabled() {
		return Defaults.SSL_ENABLED;
	}

	@Override
	public String getKeyStoreType() {
		return Defaults.SSL_KEYSTORE_TYPE;
	}

	@Override
	public String getSslProtocol() {
		return Defaults.SSL_PROTOCOL;
	}

	@Override
	public String getKeyManagerFactoryAlgorithm() {
		return Defaults.SSL_KMF_ALGORITHM;
	}

	@Override
	public String getTrustManagerFactoryAlgorithm() {
		return Defaults.SSL_TMF_ALGORITHM;
	}

	@Override
	public int getMaxThreadCount() {
		return Defaults.MAX_THREAD_COUNT;
	}

	@Override
	public String getRequestLoggingLevel() {
		return Defaults.REQUEST_LOGGING_LEVEL.getName();
	}

	@Override
	public boolean isCompressionEnabled() {
		return Defaults.COMPRESSION_ENABLED;
	}

}
