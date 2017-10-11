package it.albertus.httpserver.config;

import java.util.logging.Level;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.Filter;

import it.albertus.httpserver.filter.DateResponseFilter;
import it.albertus.httpserver.filter.GzipRequestFilter;

@SuppressWarnings("restriction")
public abstract class HttpServerDefaultConfig implements IHttpServerConfig {

	public static final int PORT = 8080;
	public static final boolean ENABLED = true;
	public static final int MAX_THREAD_COUNT = 15;
	public static final int MIN_THREAD_COUNT = MAX_THREAD_COUNT / 10;
	public static final long THREAD_KEEP_ALIVE_TIME = 60L; // seconds
	public static final boolean SSL_ENABLED = false;
	public static final String SSL_KEYSTORE_TYPE = "JKS";
	public static final String SSL_PROTOCOL = "TLS";
	public static final String SSL_KMF_ALGORITHM = KeyManagerFactory.getDefaultAlgorithm();
	public static final String SSL_TMF_ALGORITHM = TrustManagerFactory.getDefaultAlgorithm();
	public static final long MAX_REQ_TIME = 60L; // seconds
	public static final long MAX_RSP_TIME = 60L * 60 * 24; // seconds
	public static final String REQUEST_LOGGING_LEVEL = Level.INFO.getName();
	public static final boolean COMPRESSION_ENABLED = true;
	public static final int RESPONSE_BUFFER_LIMIT = 512 * 1024; // 512 KiB
	public static final int STOP_DELAY = 0; // seconds
	public static final boolean TRACE_METHOD_ENABLED = false;

	@Override
	public Filter[] getFilters() {
		return new Filter[] { new GzipRequestFilter(), new DateResponseFilter() };
	}

	@Override
	public Authenticator getAuthenticator() {
		return null; // no authentication
	}

	@Override
	public boolean isEnabled() {
		return ENABLED;
	}

	@Override
	public int getPort() {
		return PORT;
	}

	/**
	 * Returns the maximum time a request is allowed to take.
	 * 
	 * @return {@link #MAX_REQ_TIME}.
	 */
	@Override
	public long getMaxReqTime() {
		return MAX_REQ_TIME;
	}

	/**
	 * Returns the maximum time a response is allowed to take.
	 * 
	 * @return {@link #MAX_RSP_TIME}.
	 */
	@Override
	public long getMaxRspTime() {
		return MAX_RSP_TIME;
	}

	@Override
	public boolean isSslEnabled() {
		return SSL_ENABLED;
	}

	@Override
	public String getKeyStoreType() {
		return SSL_KEYSTORE_TYPE;
	}

	@Override
	public String getSslProtocol() {
		return SSL_PROTOCOL;
	}

	@Override
	public String getKeyManagerFactoryAlgorithm() {
		return SSL_KMF_ALGORITHM;
	}

	@Override
	public String getTrustManagerFactoryAlgorithm() {
		return SSL_TMF_ALGORITHM;
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

	/**
	 * Returns the maximum number of threads to allow in the pool.
	 * 
	 * @return {@link #MAX_THREAD_COUNT}
	 */
	@Override
	public int getMaxThreadCount() {
		return MAX_THREAD_COUNT;
	}

	/**
	 * Returns the number of threads to keep in the pool, even if they are idle.
	 * 
	 * @return {@link #getMaxThreadCount()} / 10.
	 */
	@Override
	public int getMinThreadCount() {
		return getMaxThreadCount() / 10;
	}

	@Override
	public long getThreadKeepAliveTime() {
		return THREAD_KEEP_ALIVE_TIME;
	}

	@Override
	public String getRequestLoggingLevel() {
		return REQUEST_LOGGING_LEVEL;
	}

	@Override
	public boolean isCompressionEnabled() {
		return COMPRESSION_ENABLED;
	}

	@Override
	public int getResponseBufferLimit() {
		return RESPONSE_BUFFER_LIMIT;
	}

	@Override
	public int getStopDelay() {
		return STOP_DELAY;
	}

	@Override
	public char[] getStorePass() {
		return null;
	}

	@Override
	public String getKeyStoreFileName() {
		return null;
	}

	@Override
	public char[] getKeyPass() {
		return null;
	}

	@Override
	public boolean isTraceMethodEnabled() {
		return TRACE_METHOD_ENABLED;
	}

}
