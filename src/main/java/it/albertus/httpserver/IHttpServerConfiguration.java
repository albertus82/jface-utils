package it.albertus.httpserver;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;

public interface IHttpServerConfiguration {

	boolean isEnabled();

	boolean isAuthenticationRequired();

	String getRealm();

	String getUsername();

	char[] getPassword();

	String getPasswordHashAlgorithm();

	int getPort();

	/**
	 * Returns the maximum time a request is allowed to take, in seconds.
	 * 
	 * @return the maximum time a request is allowed to take, in seconds.
	 */
	long getMaxReqTime();

	/**
	 * Returns the maximum time a response is allowed to take, in seconds.
	 * 
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

	int getThreadCount();

	/**
	 * Returns the maximum time that idle threads will wait for new tasks before
	 * terminating, in seconds.
	 * 
	 * @return the maximum time that idle threads will wait for new tasks before
	 *         terminating, in seconds.
	 */
	long getThreadKeepAliveTime();

	String getRequestLoggingLevel();

	boolean isCompressionEnabled();

}
