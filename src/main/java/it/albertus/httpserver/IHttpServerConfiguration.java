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

	long getMaxReqTime();

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

	int getMaxThreadCount();

	String getRequestLoggingLevel();

	boolean isCompressionEnabled();

}
