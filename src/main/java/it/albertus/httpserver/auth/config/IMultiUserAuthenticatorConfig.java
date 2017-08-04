package it.albertus.httpserver.auth.config;

import javax.annotation.Nullable;

public interface IMultiUserAuthenticatorConfig extends IUserAuthenticatorConfig {

	/**
	 * Returns the expected password or the expected password hash for the
	 * provided username, depending on the value returned by
	 * {@link #getPasswordHashAlgorithm()}.
	 * 
	 * @param username the username of which retrieve the password.
	 * 
	 * @return the password or the password hash, or null if the username
	 *         doesn't exist.
	 */
	@Nullable
	char[] getPassword(String username);

}
