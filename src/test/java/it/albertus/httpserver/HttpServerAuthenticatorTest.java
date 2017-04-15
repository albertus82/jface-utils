package it.albertus.httpserver;

import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import it.albertus.util.Configured;
import it.albertus.util.NewLine;

public class HttpServerAuthenticatorTest {

	private static Configured<String> username;

	@BeforeClass
	public static void init() {
		username = new Configured<String>() {
			@Override
			public String getValue() {
				return "qwertyuiop";
			}
		};
	}

	@Test
	public void testCheckCredentialsNoHash() throws NoSuchAlgorithmException {
		final Configured<char[]> password = new Configured<char[]>() {
			@Override
			public char[] getValue() {
				return "1234567890".toCharArray();
			}
		};
		final HttpServerAuthenticator authenticator = new HttpServerAuthenticator(null, username, password);
		authenticator.setFailDelayInMillis(0);

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
		final Configured<char[]> password = new Configured<char[]>() {
			@Override
			public char[] getValue() {
				return "e807f1fcf82d132f9bb018ca6738a19f".toCharArray();
			}
		};
		final HttpServerAuthenticator authenticator = new HttpServerAuthenticator(null, username, password, "MD5");
		authenticator.setFailDelayInMillis(0);

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
		final Configured<char[]> password = new Configured<char[]>() {
			@Override
			public char[] getValue() {
				return "01b307acba4f54f55aafc33bb06bbbf6ca803e9a".toCharArray();
			}
		};
		final HttpServerAuthenticator authenticator = new HttpServerAuthenticator(null, username, password, "SHA-1");
		authenticator.setFailDelayInMillis(0);

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
		final Configured<char[]> password = new Configured<char[]>() {
			@Override
			public char[] getValue() {
				return "c775e7b757ede630cd0aa1113bd102661ab38829ca52a6422ab782862f268646".toCharArray();
			}
		};
		final HttpServerAuthenticator authenticator = new HttpServerAuthenticator(null, username, password, "SHA-256");
		authenticator.setFailDelayInMillis(0);

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
		final Configured<char[]> password = new Configured<char[]>() {
			@Override
			public char[] getValue() {
				return "ed845f8b4f2a6d5da86a3bec90352d916d6a66e3420d720e16439adf238f129182c8c64fc4ec8c1e6506bc2b4888baf9".toCharArray();
			}
		};
		final HttpServerAuthenticator authenticator = new HttpServerAuthenticator(null, username, password, "SHA-384");
		authenticator.setFailDelayInMillis(0);

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
		final Configured<char[]> password = new Configured<char[]>() {
			@Override
			public char[] getValue() {
				return "12b03226a6d8be9c6e8cd5e55dc6c7920caaa39df14aab92d5e3ea9340d1c8a4d3d0b8e4314f1f6ef131ba4bf1ceb9186ab87c801af0d5c95b1befb8cedae2b9".toCharArray();
			}
		};
		final HttpServerAuthenticator authenticator = new HttpServerAuthenticator(null, username, password, "SHA-512");
		authenticator.setFailDelayInMillis(0);

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
		final Configured<char[]> password = new Configured<char[]>() {
			@Override
			public char[] getValue() {
				return "1234567890".toCharArray();
			}
		};
		final HttpServerAuthenticator authenticator = new HttpServerAuthenticator(null, username, password);

		for (int i = 0; i < 10; i++) {
			final int delay = (int) (Math.random() * 100);
			authenticator.setFailDelayInMillis(delay);
			final long before = System.currentTimeMillis();
			Assert.assertFalse(authenticator.checkCredentials(Integer.toString(delay), Integer.toString(delay)));
			final long after = System.currentTimeMillis();
			System.out.printf("Elapsed time: %s ms%s", (after - before), NewLine.SYSTEM_LINE_SEPARATOR);
			Assert.assertTrue(after - before >= delay);
		}
	}

}
