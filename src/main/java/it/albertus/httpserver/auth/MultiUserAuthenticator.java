package it.albertus.httpserver.auth;

import java.util.logging.Level;
import java.util.logging.Logger;

import it.albertus.httpserver.auth.config.IMultiUserAuthenticatorConfig;
import it.albertus.jface.JFaceMessages;
import it.albertus.util.logging.LoggerFactory;

public class MultiUserAuthenticator extends AbstractUserAuthenticator {

	private static final Logger logger = LoggerFactory.getLogger(MultiUserAuthenticator.class);

	public MultiUserAuthenticator(final IMultiUserAuthenticatorConfig configuration) {
		super(configuration);
	}

	@Override
	public boolean checkCredentials(final String specifiedUsername, final String specifiedPassword) {
		try {
			if (specifiedUsername == null || specifiedUsername.isEmpty() || specifiedPassword == null || specifiedPassword.isEmpty()) {
				return fail();
			}

			final char[] expectedPassword = getConfiguration().getPassword(specifiedUsername);
			if (expectedPassword != null && expectedPassword.length > 0 && checkPassword(specifiedPassword, expectedPassword)) {
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
	protected IMultiUserAuthenticatorConfig getConfiguration() {
		return (IMultiUserAuthenticatorConfig) super.getConfiguration();
	}

}
