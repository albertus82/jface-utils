package it.albertus.httpserver.auth;

import java.util.logging.Level;
import java.util.logging.Logger;

import it.albertus.httpserver.auth.config.ISingleUserAuthenticatorConfig;
import it.albertus.jface.JFaceMessages;
import it.albertus.util.logging.LoggerFactory;

public class SingleUserAuthenticator extends AbstractUserAuthenticator {

	private static final Logger logger = LoggerFactory.getLogger(SingleUserAuthenticator.class);

	public SingleUserAuthenticator(final ISingleUserAuthenticatorConfig configuration) {
		super(configuration);
	}

	@Override
	public boolean checkCredentials(final String specifiedUsername, final String specifiedPassword) {
		try {
			if (specifiedUsername == null || specifiedUsername.isEmpty() || specifiedPassword == null || specifiedPassword.isEmpty()) {
				return fail();
			}

			final String expectedUsername = getConfiguration().getUsername();
			if (expectedUsername == null || expectedUsername.isEmpty()) {
				logger.warning(JFaceMessages.get("err.httpserver.configuration.username"));
				return fail();
			}

			final char[] expectedPassword = getConfiguration().getPassword();
			if (expectedPassword == null || expectedPassword.length == 0) {
				logger.warning(JFaceMessages.get("err.httpserver.configuration.password"));
				return fail();
			}

			if (specifiedUsername.equalsIgnoreCase(expectedUsername) && checkPassword(specifiedPassword, expectedPassword)) {
				return true;
			}
			else {
				logger.log(Level.parse(getConfiguration().getFailureLoggingLevel()), JFaceMessages.get("err.httpserver.authentication"), new String[] { specifiedUsername, specifiedPassword });
				return fail();
			}
		}
		catch (final Exception e) {
			logger.log(Level.SEVERE, e.toString(), e);
			return fail();
		}
	}

	@Override
	protected ISingleUserAuthenticatorConfig getConfiguration() {
		return (ISingleUserAuthenticatorConfig) super.getConfiguration();
	}

}
