package it.albertus.httpserver;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;

public interface IHttpServerConfiguration {

	AbstractHttpHandler[] getHandlers();

	boolean isEnabled();

	boolean isAuthenticationRequired();

	String getAuthenticationRealm();

	String getAuthenticationUsername();

	char[] getAuthenticationPassword();

	String getAuthenticationPasswordHashAlgorithm();

	int getAuthenticationFailDelay();

	String getAuthenticationFailureLoggingLevel();

	int getPort();

	/**
	 * @return the maximum time a request is allowed to take, in seconds.
	 */
	long getMaxReqTime();

	/**
	 * @return the maximum time a response is allowed to take, in seconds.
	 */
	long getMaxRspTime();

	boolean isSslEnabled();

	char[] getStorePass();

	String getKeyStoreType();

	String getKeyStoreFileName();

	char[] getKeyPass();

	String getSslProtocol();

	String getKeyManagerFactoryAlgorithm();

	String getTrustManagerFactoryAlgorithm();

	SSLParameters getSslParameters(SSLContext context);

	/**
	 * @return the maximum number of threads to allow in the pool.
	 */
	int getMaxThreadCount();

	/**
	 * @return the number of threads to keep in the pool, even if they are idle.
	 */
	int getMinThreadCount();

	/**
	 * @return the maximum time that excess idle threads will wait for new tasks
	 *         before terminating, in seconds.
	 */
	long getThreadKeepAliveTime();

	String getRequestLoggingLevel();

	boolean isCompressionEnabled();

}
