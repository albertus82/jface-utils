package it.albertus.httpserver;

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

	int getMaxThreadCount();

	String getRequestLoggingLevel();

	boolean isCompressionEnabled();

}
