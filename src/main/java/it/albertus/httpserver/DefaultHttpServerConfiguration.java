package it.albertus.httpserver;

import java.util.logging.Level;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

public abstract class DefaultHttpServerConfiguration implements IHttpServerConfiguration {

	public static class Defaults {
		public static final int PORT = 8080;
		public static final boolean ENABLED = false;
		public static final boolean AUTHENTICATION = true;
		public static final String PASSWORD_HASH_ALGORITHM = "SHA-256";
		public static final int THREAD_COUNT = 15;
		public static final long THREAD_KEEP_ALIVE_TIME = 60L * 10; // seconds
		public static final boolean SSL_ENABLED = false;
		public static final String SSL_KEYSTORE_TYPE = "JKS";
		public static final String SSL_PROTOCOL = "TLS";
		public static final String SSL_KMF_ALGORITHM = KeyManagerFactory.getDefaultAlgorithm();
		public static final String SSL_TMF_ALGORITHM = TrustManagerFactory.getDefaultAlgorithm();
		public static final long MAX_REQ_TIME = 60L; // seconds
		public static final long MAX_RSP_TIME = 60L * 60 * 24; // seconds
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

	/**
	 * Returns the maximum time a request is allowed to take.
	 * 
	 * @return {@link Defaults#MAX_REQ_TIME}.
	 */
	@Override
	public long getMaxReqTime() {
		return Defaults.MAX_REQ_TIME;
	}

	/**
	 * Returns the maximum time a response is allowed to take.
	 * 
	 * @return {@link Defaults#MAX_RSP_TIME}.
	 */
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

	/**
	 * Returns a copy of the SSLParameters indicating the default settings for
	 * the given SSL context.
	 *
	 * <p>
	 * The parameters will always have the ciphersuites and protocols arrays set
	 * to non-null values.
	 * 
	 * @param context the {@link javax.net.ssl.SSLContext SSLContext}.
	 * @return a copy of the SSLParameters object with the default settings
	 * @throws UnsupportedOperationException if the default SSL parameters could
	 *         not be obtained.
	 */
	@Override
	public SSLParameters getSslParameters(final SSLContext context) {
		return context.getDefaultSSLParameters();
	}

	@Override
	public int getThreadCount() {
		return Defaults.THREAD_COUNT;
	}

	@Override
	public long getThreadKeepAliveTime() {
		return Defaults.THREAD_KEEP_ALIVE_TIME;
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
