package io.github.albertus82.net.httpserver.config;

import javax.annotation.Nullable;

import io.github.albertus82.jface.JFaceMessages;

public abstract class SingleUserAuthenticatorDefaultConfig extends AuthenticatorDefaultConfig implements ISingleUserAuthenticatorConfig {

	/**
	 * Returns the expected password or the expected password hash for the provided
	 * username, depending on the value returned by
	 * {@link #getPasswordHashAlgorithm()}.
	 * 
	 * @param username the username.
	 * 
	 * @return the password or the password hash.
	 */
	@Override
	@Nullable
	public char[] getPassword(final String username) {
		final String expectedUsername = getUsername();
		if (expectedUsername == null || expectedUsername.isEmpty()) {
			throw new IllegalStateException(JFaceMessages.get("err.httpserver.configuration.username"));
		}

		final char[] expectedPassword = getPassword();
		if (expectedPassword == null || expectedPassword.length == 0) {
			throw new IllegalStateException(JFaceMessages.get("err.httpserver.configuration.password"));
		}

		if (username != null && username.equalsIgnoreCase(getUsername())) {
			return getPassword();
		}
		else {
			return null;
		}
	}

}
