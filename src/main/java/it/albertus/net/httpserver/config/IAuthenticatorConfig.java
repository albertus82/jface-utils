package it.albertus.net.httpserver.config;

import javax.annotation.Nullable;

public interface IAuthenticatorConfig {

	/**
	 * Returns the realm name, which will be displayed in the login interface.
	 * 
	 * @return the realm name.
	 */
	String getRealm();

	/**
	 * Returns the expected password, or password hash, for the provided
	 * username, depending on the value returned by
	 * {@link #getPasswordHashAlgorithm()}.
	 * 
	 * @param username the username of which retrieve the password.
	 * 
	 * @return the password or password hash, or null if the username doesn't
	 *         exist.
	 */
	@Nullable
	char[] getPassword(String username);

	/**
	 * Returns the password hashing algoritm (SHA-1, SHA-256, etc.), or null
	 * when the password is returned as it is.
	 * 
	 * @return the password hashing algorithm, or null if no hash is applied.
	 */
	@Nullable
	String getPasswordHashAlgorithm();

	/**
	 * Returns the time to wait after a failed login; useful to mitigate
	 * brute-force attacks.
	 * 
	 * @return the delay in milliseconds.
	 */
	int getFailDelayMillis();

	/**
	 * Returns the logging level for login failures (wrong credentials).
	 * 
	 * @return the level name (INFO, WARNING, etc.).
	 */
	String getFailureLoggingLevel();

}
