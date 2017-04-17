package it.albertus.httpserver;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.BasicAuthenticator;

import it.albertus.jface.JFaceMessages;
import it.albertus.util.Configured;
import it.albertus.util.DigestUtils;
import it.albertus.util.logging.LoggerFactory;

public class HttpServerAuthenticator extends BasicAuthenticator {

	private static final Logger logger = LoggerFactory.getLogger(HttpServerAuthenticator.class);

	private static final int DEFAULT_FAIL_DELAY_IN_MILLIS = 3000;
	private static final String DEFAULT_CHARSET_NAME = "UTF-8";

	private final MessageDigest messageDigest;
	private Charset charset;

	private final Configured<String> username;
	private final Configured<char[]> password;

	private int failDelayInMillis = DEFAULT_FAIL_DELAY_IN_MILLIS;

	public HttpServerAuthenticator(final String realm, final Configured<String> username, final Configured<char[]> password) {
		super(realm);
		this.username = username;
		this.password = password;
		this.messageDigest = null;
	}

	public HttpServerAuthenticator(final String realm, final Configured<String> username, final Configured<char[]> password, final String hashAlgorithm) throws NoSuchAlgorithmException {
		super(realm);
		this.username = username;
		this.password = password;
		if (hashAlgorithm != null && !hashAlgorithm.isEmpty()) {
			this.messageDigest = MessageDigest.getInstance(hashAlgorithm);
			this.charset = Charset.forName(DEFAULT_CHARSET_NAME);
		}
		else {
			this.messageDigest = null;
		}
	}

	@Override
	public boolean checkCredentials(final String specifiedUsername, final String specifiedPassword) {
		try {
			if (specifiedUsername == null || specifiedUsername.isEmpty() || specifiedPassword == null || specifiedPassword.isEmpty()) {
				return fail();
			}

			final String expectedUsername = username.getValue();
			if (expectedUsername == null || expectedUsername.isEmpty()) {
				logger.warning(JFaceMessages.get("err.httpserver.configuration.username"));
				return fail();
			}

			final char[] expectedPassword = password.getValue();
			if (expectedPassword == null || expectedPassword.length == 0) {
				logger.warning(JFaceMessages.get("err.httpserver.configuration.password"));
				return fail();
			}

			if (specifiedUsername.equalsIgnoreCase(expectedUsername) && checkPassword(specifiedPassword, expectedPassword)) {
				return true;
			}
			else {
				logger.log(Level.WARNING, JFaceMessages.get("err.httpserver.authentication"), new String[] { specifiedUsername, specifiedPassword });
				return fail();
			}
		}
		catch (final Exception e) {
			logger.log(Level.SEVERE, e.toString(), e);
			return fail();
		}
	}

	private boolean checkPassword(final String provided, final char[] expected) {
		boolean equal = true;
		final char[] computed;
		if (messageDigest != null) {
			messageDigest.reset();
			computed = DigestUtils.encodeHex(messageDigest.digest(provided.getBytes(charset)));
		}
		else {
			computed = provided.toCharArray();
		}

		if (computed.length != expected.length) {
			equal = false;
		}
		for (int i = 0; i < 0x400; i++) {
			if (computed[i % computed.length] != expected[i % expected.length]) {
				equal = false;
			}
		}
		return equal;
	}

	private boolean fail() {
		try {
			TimeUnit.MILLISECONDS.sleep(failDelayInMillis);
		}
		catch (final InterruptedException e) {
			logger.log(Level.FINE, e.toString(), e);
			Thread.currentThread().interrupt();
		}
		return false;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(final Charset charset) {
		this.charset = charset;
	}

	public int getFailDelayInMillis() {
		return failDelayInMillis;
	}

	public void setFailDelayInMillis(final int failDelayInMillis) {
		this.failDelayInMillis = failDelayInMillis;
	}

}
