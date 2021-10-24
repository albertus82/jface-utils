package it.albertus.net.httpserver.config;

import java.util.logging.Level;

public abstract class AuthenticatorDefaultConfig implements IAuthenticatorConfig {

	public static final String PASSWORD_HASH_ALGORITHM = "SHA-256"; // NOSONAR
	public static final int FAIL_DELAY_MILLIS = 3000;
	public static final String FAILURE_LOGGING_LEVEL = Level.FINE.getName();

	@Override
	public String getPasswordHashAlgorithm() {
		return PASSWORD_HASH_ALGORITHM;
	}

	@Override
	public int getFailDelayMillis() {
		return FAIL_DELAY_MILLIS;
	}

	@Override
	public String getFailureLoggingLevel() {
		return FAILURE_LOGGING_LEVEL;
	}

}
