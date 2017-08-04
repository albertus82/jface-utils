package it.albertus.httpserver.auth.config;

import javax.annotation.Nullable;

public interface IUserAuthenticatorConfig {

	/**
	 * Returns the realm name, which will be displayed in the login interface.
	 * 
	 * @return the realm name.
	 */
	String getRealm();

	/**
	 * Returns the password hashing algoritm (SHA-1, SHA-256, etc.) or null when
	 * the password is returned as it is.
	 * 
	 * @return the password hashing algorithm, or null if no hash is used.
	 */
	@Nullable
	String getPasswordHashAlgorithm();

	/**
	 * Returns the time to wait after a failed login; useful to mitigate
	 * brute-force attacks.
	 * 
	 * @return the delay in millis.
	 */
	int getFailDelay();

	/**
	 * Returns the logging level for login failures (wrong credentials).
	 * 
	 * @return the level name (INFO, WARNING, etc.).
	 */
	String getFailureLoggingLevel();

}
