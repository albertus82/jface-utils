package it.albertus.httpserver.auth.config;

import java.util.logging.Level;

public abstract class UserAuthenticatorDefaultConfig implements IUserAuthenticatorConfig {

	public static final String DEFAULT_PASSWORD_HASH_ALGORITHM = "SHA-256";
	public static final int DEFAULT_FAIL_DELAY = 3000;
	public static final String DEFAULT_FAILURE_LOGGING_LEVEL = Level.FINE.getName();

	@Override
	public String getPasswordHashAlgorithm() {
		return DEFAULT_PASSWORD_HASH_ALGORITHM;
	}

	@Override
	public int getFailDelay() {
		return DEFAULT_FAIL_DELAY;
	}

	@Override
	public String getFailureLoggingLevel() {
		return DEFAULT_FAILURE_LOGGING_LEVEL;
	}

}
