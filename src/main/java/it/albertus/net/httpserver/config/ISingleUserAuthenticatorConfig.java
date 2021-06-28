package it.albertus.net.httpserver.config;

public interface ISingleUserAuthenticatorConfig extends IAuthenticatorConfig {

	/**
	 * Returns the expected username.
	 * 
	 * @return the username.
	 */
	String getUsername();

	/**
	 * Returns the expected password or the expected password hash, depending on the
	 * value returned by {@link #getPasswordHashAlgorithm()}.
	 * 
	 * @return the password or the password hash.
	 */
	char[] getPassword();

}
