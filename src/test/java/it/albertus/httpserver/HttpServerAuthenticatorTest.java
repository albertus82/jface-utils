package it.albertus.httpserver;

import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Test;

import it.albertus.util.NewLine;

public class HttpServerAuthenticatorTest {

	private static class HttpServerConfiguration extends DefaultHttpServerConfiguration {

		private char[] password;
		private String algorithm;
		private int delay;
		private String realm;

		@Override
		public AbstractHttpHandler[] getHandlers() {
			return null;
		}

		@Override
		public String getAuthenticationUsername() {
			return "qwertyuiop";
		};

		@Override
		public char[] getAuthenticationPassword() {
			return password;
		};

		@Override
		public String getAuthenticationPasswordHashAlgorithm() {
			return algorithm;
		}

		@Override
		public int getAuthenticationFailDelay() {
			return delay;
		}

		@Override
		public String getAuthenticationRealm() {
			return realm;
		}

		protected void setPassword(String password) {
			this.password = password.toCharArray();
		}

		protected void setAlgorithm(String algorithm) {
			this.algorithm = algorithm;
		}

		protected void setDelay(int delay) {
			this.delay = delay;
		}

		protected void setRealm(String realm) {
			this.realm = realm;
		}

	};

	private static HttpServerConfiguration configuration = new HttpServerConfiguration();

	@Test
	public void testCheckCredentialsNoHash() {
		configuration.setAlgorithm(null);
		configuration.setPassword("1234567890");
		configuration.setRealm(null);
		configuration.setDelay(0);
		final HttpServerAuthenticator authenticator = new HttpServerAuthenticator(configuration);

		Assert.assertTrue(authenticator.checkCredentials("qwertyuiop", "1234567890"));
		Assert.assertTrue(authenticator.checkCredentials("QwErTyUiOp", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("wertyuiop", "123456789"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop", "123456789"));
		Assert.assertFalse(authenticator.checkCredentials(" qwertyuiop", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop  ", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials(" qwertyuiop ", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop ", "123456789 "));
	}

	@Test
	public void testCheckCredentialsMd5() throws NoSuchAlgorithmException {
		configuration.setAlgorithm("MD5");
		configuration.setPassword("e807f1fcf82d132f9bb018ca6738a19f");
		configuration.setRealm(null);
		configuration.setDelay(0);
		final HttpServerAuthenticator authenticator = new HttpServerAuthenticator(configuration);

		Assert.assertTrue(authenticator.checkCredentials("qwertyuiop", "1234567890"));
		Assert.assertTrue(authenticator.checkCredentials("QwErTyUiOp", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("wertyuiop", "123456789"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop", "123456789"));
		Assert.assertFalse(authenticator.checkCredentials(" qwertyuiop", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop  ", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials(" qwertyuiop ", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop ", "123456789 "));
	}

	@Test
	public void testCheckCredentialsSha1() throws NoSuchAlgorithmException {
		configuration.setAlgorithm("SHA-1");
		configuration.setPassword("01b307acba4f54f55aafc33bb06bbbf6ca803e9a");
		configuration.setRealm(null);
		configuration.setDelay(0);
		final HttpServerAuthenticator authenticator = new HttpServerAuthenticator(configuration);

		Assert.assertTrue(authenticator.checkCredentials("qwertyuiop", "1234567890"));
		Assert.assertTrue(authenticator.checkCredentials("QwErTyUiOp", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("wertyuiop", "123456789"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop", "123456789"));
		Assert.assertFalse(authenticator.checkCredentials(" qwertyuiop", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop  ", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials(" qwertyuiop ", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop ", "123456789 "));
	}

	@Test
	public void testCheckCredentialsSha256() throws NoSuchAlgorithmException {
		configuration.setAlgorithm("SHA-256");
		configuration.setPassword("c775e7b757ede630cd0aa1113bd102661ab38829ca52a6422ab782862f268646");
		configuration.setRealm("TEST REALM");
		configuration.setDelay(0);
		final HttpServerAuthenticator authenticator = new HttpServerAuthenticator(configuration);

		Assert.assertTrue(authenticator.checkCredentials("qwertyuiop", "1234567890"));
		Assert.assertTrue(authenticator.checkCredentials("QwErTyUiOp", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("wertyuiop", "123456789"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop", "123456789"));
		Assert.assertFalse(authenticator.checkCredentials(" qwertyuiop", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop  ", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials(" qwertyuiop ", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop ", "123456789 "));
	}

	@Test
	public void testCheckCredentialsSha384() throws NoSuchAlgorithmException {
		configuration.setAlgorithm("SHA-384");
		configuration.setPassword("ed845f8b4f2a6d5da86a3bec90352d916d6a66e3420d720e16439adf238f129182c8c64fc4ec8c1e6506bc2b4888baf9");
		configuration.setRealm(null);
		configuration.setDelay(0);
		final HttpServerAuthenticator authenticator = new HttpServerAuthenticator(configuration);

		Assert.assertTrue(authenticator.checkCredentials("qwertyuiop", "1234567890"));
		Assert.assertTrue(authenticator.checkCredentials("QwErTyUiOp", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("wertyuiop", "123456789"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop", "123456789"));
		Assert.assertFalse(authenticator.checkCredentials(" qwertyuiop", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop  ", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials(" qwertyuiop ", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop ", "123456789 "));
	}

	@Test
	public void testCheckCredentialsSha512() throws NoSuchAlgorithmException {
		configuration.setAlgorithm("SHA-512");
		configuration.setPassword("12b03226a6d8be9c6e8cd5e55dc6c7920caaa39df14aab92d5e3ea9340d1c8a4d3d0b8e4314f1f6ef131ba4bf1ceb9186ab87c801af0d5c95b1befb8cedae2b9");
		configuration.setRealm(null);
		configuration.setDelay(0);
		final HttpServerAuthenticator authenticator = new HttpServerAuthenticator(configuration);

		Assert.assertTrue(authenticator.checkCredentials("qwertyuiop", "1234567890"));
		Assert.assertTrue(authenticator.checkCredentials("QwErTyUiOp", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("wertyuiop", "123456789"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop", "123456789"));
		Assert.assertFalse(authenticator.checkCredentials(" qwertyuiop", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop  ", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials(" qwertyuiop ", "1234567890"));
		Assert.assertFalse(authenticator.checkCredentials("qwertyuiop ", "123456789 "));
	}

	@Test
	public void testFailDelay() {
		configuration.setAlgorithm(null);
		configuration.setPassword("1234567890");
		configuration.setRealm(null);
		final HttpServerAuthenticator authenticator = new HttpServerAuthenticator(configuration);

		for (int i = 0; i < 10; i++) {
			final int delay = (int) (Math.random() * 100);
			configuration.setDelay(delay);
			final long before = System.currentTimeMillis();
			Assert.assertFalse(authenticator.checkCredentials(Integer.toString(delay), Integer.toString(delay)));
			final long after = System.currentTimeMillis();
			System.out.printf("Elapsed time: %s ms%s", (after - before), NewLine.SYSTEM_LINE_SEPARATOR);
			Assert.assertTrue(after - before >= delay);
		}
	}

}
