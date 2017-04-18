package it.albertus.httpserver;

public interface IHttpServerConfiguration {

	boolean isEnabled();

	boolean isAuthenticationRequired();

	String getRealm();

	String getUsername();

	char[] getPassword();

	String getPasswordHashAlgorithm();

	int getPort();

	short getMaxReqTime();

	short getMaxRspTime();

	boolean isSslEnabled();

	char[] getStorePass();

	String getKeyStoreType();

	String getKeyStoreFileName();

	char[] getKeyPass();

	String getSslProtocol();

	String getKeyManagerFactoryAlgorithm();

	String getTrustManagerFactoryAlgorithm();

	byte getThreadCount();

	String getRequestLoggingLevel();

	boolean isCompressionEnabled();

}
