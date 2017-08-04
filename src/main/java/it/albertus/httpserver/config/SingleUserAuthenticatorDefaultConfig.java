package it.albertus.httpserver.config;

import javax.annotation.Nullable;

import it.albertus.jface.JFaceMessages;

public abstract class SingleUserAuthenticatorDefaultConfig extends AuthenticatorDefaultConfig implements ISingleUserAuthenticatorConfig {

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
