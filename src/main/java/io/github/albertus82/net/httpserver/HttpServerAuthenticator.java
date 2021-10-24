package io.github.albertus82.net.httpserver;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpExchange;

import io.github.albertus82.jface.JFaceMessages;
import io.github.albertus82.net.httpserver.config.IAuthenticatorConfig;
import io.github.albertus82.util.StringUtils;
import io.github.albertus82.util.logging.LoggerFactory;

@SuppressWarnings("restriction")
public class HttpServerAuthenticator extends BasicAuthenticator {

	private static final Logger log = LoggerFactory.getLogger(HttpServerAuthenticator.class);

	private static final String DEFAULT_CHARSET_NAME = "UTF-8";

	private final IAuthenticatorConfig configuration;
	private Charset charset = Charset.forName(DEFAULT_CHARSET_NAME);

	private final ThreadLocal<HttpExchange> exchanges = new ThreadLocal<HttpExchange>();

	public HttpServerAuthenticator(final IAuthenticatorConfig configuration) {
		super(configuration.getRealm());
		this.configuration = configuration;
	}

	@Override
	public Result authenticate(final HttpExchange exchange) {
		try {
			exchanges.set(exchange); // used in checkCredentials(...)
			return super.authenticate(exchange);
		}
		finally {
			exchanges.remove();
		}
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
				final HttpExchange exchange = exchanges.get();
				log.log(Level.parse(getConfiguration().getFailureLoggingLevel()), JFaceMessages.get("err.httpserver.authentication"), new Object[] { specifiedUsername, specifiedPassword, exchange != null ? exchange.getRemoteAddress() : null });
				return fail();
			}
		}
		catch (final Exception e) {
			log.log(Level.SEVERE, "An error occurred while checking credentials:", e);
			return fail();
		}
	}

	protected boolean checkPassword(final String provided, final char[] expected) {
		final char[] computed;
		if (StringUtils.isNotBlank(configuration.getPasswordHashAlgorithm())) {
			computed = DatatypeConverter.printHexBinary(newMessageDigest(configuration.getPasswordHashAlgorithm().trim()).digest(provided.getBytes(charset))).toLowerCase(Locale.ROOT).toCharArray();
		}
		else {
			computed = provided.toCharArray();
		}

		boolean equal = true;
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

	protected boolean fail() {
		try {
			TimeUnit.MILLISECONDS.sleep(configuration.getFailDelayMillis());
		}
		catch (final InterruptedException e) {
			log.log(Level.FINE, "Sleep interrupted:", e);
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

	protected IAuthenticatorConfig getConfiguration() {
		return configuration;
	}

	private static MessageDigest newMessageDigest(final String algorithm) {
		try {
			return MessageDigest.getInstance(algorithm);
		}
		catch (final NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(algorithm, e);
		}
	}

}
