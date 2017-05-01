package it.albertus.httpserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.xml.bind.DatatypeConverter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.net.httpserver.HttpExchange;

import it.albertus.httpserver.annotation.Path;
import it.albertus.util.IOUtils;
import it.albertus.util.logging.LoggerFactory;

public class LightweightHttpServerTest {

	private static final Logger logger = LoggerFactory.getLogger(LightweightHttpServerTest.class);

	private static final String JKS = "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";
	private static final int PORT = 8888;
	private static final String USERNAME = "test";
	// private static final String PASSWORD = "TESTtest12345";
	private static final String PASSWORD_HASH = "92f1a57051141e9d24396bc42ae43b6500d13f8b";
	private static final String REALM = "Test Realm";
	private static final String CREDENTIALS_BASE64 = "dGVzdDpURVNUdGVzdDEyMzQ1";

	private static final String HANDLER_PATH_TXT = "/loremIpsum.txt";
	private static final String HANDLER_PATH_DISABLED = "/disabled.txt";

	private static final String payload = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
	private static final String payloadMd5 = "db89bb5ceab87f9c0fcc2ab36c189c2c";

	private static boolean authenticationRequired = false;
	private static boolean sslEnabled = false;

	private static LightweightHttpServer server;
	private static File certificate;

	@Path(HANDLER_PATH_DISABLED)
	private static class DisabledHandler extends AbstractHttpHandler {
		@Override
		protected void doGet(final HttpExchange exchange) throws IOException, HttpException {
			sendResponse(exchange, payload.getBytes());
		}
	}

	@BeforeClass
	public static void init() throws InterruptedException, IOException {
		certificate = File.createTempFile("cert-", ".jks");
		logger.info(certificate.toString());
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(certificate);
			fos.write(DatatypeConverter.parseBase64Binary(JKS));
		}
		finally {
			IOUtils.closeQuietly(fos);
		}

		final IHttpServerConfiguration configuration = new DefaultHttpServerConfiguration() {
			@Override
			public AbstractHttpHandler[] getHandlers() {
				final AbstractHttpHandler h1 = new AbstractHttpHandler() {
					@Override
					protected void doGet(final HttpExchange exchange) throws IOException, HttpException {
						sendResponse(exchange, payload.getBytes());
					}
				};
				h1.setPath(HANDLER_PATH_TXT);
				final AbstractHttpHandler h2 = new DisabledHandler();
				h2.setEnabled(false);
				return new AbstractHttpHandler[] { h1, h2 };
			}

			@Override
			public int getPort() {
				return PORT;
			}

			@Override
			public boolean isAuthenticationRequired() {
				return authenticationRequired;
			}

			@Override
			public String getUsername() {
				return USERNAME;
			}

			@Override
			public char[] getPassword() {
				return PASSWORD_HASH.toCharArray();
			}

			@Override
			public String getRealm() {
				return REALM;
			}

			@Override
			public String getPasswordHashAlgorithm() {
				return "SHA-1";
			}

			@Override
			public boolean isSslEnabled() {
				return sslEnabled;
			}

			@Override
			public char[] getStorePass() {
				return "storepass".toCharArray();
			}

			@Override
			public String getKeyStoreFileName() {
				return certificate.getPath();
			}

			@Override
			public char[] getKeyPass() {
				return "keypass".toCharArray();
			}
		};
		server = new LightweightHttpServer(configuration);
	}

	@Test
	public void makeRequest200() throws IOException, InterruptedException {
		authenticationRequired = false;
		sslEnabled = false;
		startServer();
		final URL url = new URL("http://localhost:8888" + HANDLER_PATH_TXT);
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(20000);
		connection.setReadTimeout(20000);
		Assert.assertEquals(200, connection.getResponseCode());
		Assert.assertEquals(payloadMd5, connection.getHeaderField("ETAG"));
		Assert.assertTrue(connection.getContentType().startsWith("text/plain"));
		Assert.assertNotEquals(0, connection.getDate());
		InputStream is = null;
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			is = connection.getInputStream();
			IOUtils.copy(is, os, payload.length() / 3);
		}
		finally {
			IOUtils.closeQuietly(os, is);
		}
		connection.disconnect();
		Assert.assertEquals(payload, os.toString());
	}

	@Test
	public void makeRequest304() throws IOException, InterruptedException {
		authenticationRequired = false;
		sslEnabled = false;
		startServer();
		final URL url = new URL("http://localhost:8888" + HANDLER_PATH_TXT);
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(20000);
		connection.setReadTimeout(20000);
		connection.addRequestProperty("If-None-Match", payloadMd5);
		Assert.assertEquals(304, connection.getResponseCode());
		Assert.assertNotEquals(0, connection.getDate());
		Assert.assertEquals(payloadMd5, connection.getHeaderField("Etag"));
		InputStream is = null;
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			is = connection.getInputStream();
			IOUtils.copy(is, os, payload.length() / 3);
		}
		finally {
			IOUtils.closeQuietly(os, is);
		}
		connection.disconnect();
		Assert.assertEquals(true, os.toString().isEmpty());
	}

	@Test
	public void makeRequest401() throws IOException, InterruptedException {
		authenticationRequired = true;
		sslEnabled = false;
		startServer();
		final URL url = new URL("http://localhost:8888" + HANDLER_PATH_TXT);
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(20000);
		connection.setReadTimeout(20000);
		Assert.assertEquals(401, connection.getResponseCode());
		connection.disconnect();
	}

	@Test
	public void makeRequests200Auth() throws IOException, InterruptedException {
		authenticationRequired = true;
		sslEnabled = false;
		startServer();
		final URL url = new URL("http://localhost:8888" + HANDLER_PATH_TXT);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(20000);
		connection.setReadTimeout(20000);
		Assert.assertEquals(401, connection.getResponseCode());
		Assert.assertEquals("Basic realm=\"" + REALM + '"', connection.getHeaderField("WWW-Authenticate"));
		connection.disconnect();
		connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(20000);
		connection.setReadTimeout(20000);
		connection.addRequestProperty("Authorization", "Basic " + CREDENTIALS_BASE64);
		Assert.assertEquals(200, connection.getResponseCode());
		Assert.assertEquals(payloadMd5, connection.getHeaderField("Etag"));
		Assert.assertTrue(connection.getContentType().startsWith("text/plain"));
		Assert.assertNotEquals(0, connection.getDate());
		InputStream is = null;
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			is = connection.getInputStream();
			IOUtils.copy(is, os, payload.length() / 3);
		}
		finally {
			IOUtils.closeQuietly(os, is);
		}
		connection.disconnect();
		Assert.assertEquals(payload, os.toString());
	}

	@Test
	public void makeRequest403() throws IOException, InterruptedException {
		authenticationRequired = false;
		sslEnabled = false;
		startServer();
		final URL url = new URL("http://localhost:8888" + HANDLER_PATH_DISABLED);
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(20000);
		connection.setReadTimeout(20000);
		Assert.assertEquals(403, connection.getResponseCode());
		Assert.assertNotEquals(0, connection.getDate());
		connection.disconnect();
	}

	@Test
	public void makeRequest404() throws IOException, InterruptedException {
		authenticationRequired = false;
		sslEnabled = false;
		startServer();
		final URL url = new URL("http://localhost:8888" + "/qwertyuiop");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(20000);
		connection.setReadTimeout(20000);
		Assert.assertEquals(404, connection.getResponseCode());
		connection.disconnect();
	}

	@Test
	public void makeRequests304Auth() throws IOException, InterruptedException {
		authenticationRequired = true;
		sslEnabled = false;
		startServer();
		final URL url = new URL("http://localhost:8888" + HANDLER_PATH_TXT);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(20000);
		connection.setReadTimeout(20000);
		Assert.assertEquals(401, connection.getResponseCode());
		Assert.assertEquals("Basic realm=\"" + REALM + '"', connection.getHeaderField("WWW-Authenticate"));
		connection.disconnect();
		connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(20000);
		connection.setReadTimeout(20000);
		connection.addRequestProperty("Authorization", "Basic " + CREDENTIALS_BASE64);
		connection.addRequestProperty("If-None-Match", payloadMd5);
		Assert.assertEquals(304, connection.getResponseCode());
		Assert.assertNotEquals(0, connection.getDate());
		Assert.assertEquals(payloadMd5, connection.getHeaderField("Etag"));
		InputStream is = null;
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			is = connection.getInputStream();
			IOUtils.copy(is, os, payload.length() / 3);
		}
		finally {
			IOUtils.closeQuietly(os, is);
		}
		connection.disconnect();
		Assert.assertEquals(true, os.toString().isEmpty());
	}

	@Test
	public void makeRequest200Https() throws IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException {
		authenticationRequired = false;
		sslEnabled = true;
		startServer();
		configureSsl();
		final URL url = new URL("https://localhost:8888" + HANDLER_PATH_TXT);
		final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setConnectTimeout(20000);
		connection.setReadTimeout(20000);
		Assert.assertEquals(200, connection.getResponseCode());
		Assert.assertEquals(payloadMd5, connection.getHeaderField("Etag"));
		Assert.assertTrue(connection.getContentType().startsWith("text/plain"));
		Assert.assertNotEquals(0, connection.getDate());
		InputStream is = null;
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			is = connection.getInputStream();
			IOUtils.copy(is, os, payload.length() / 3);
		}
		finally {
			IOUtils.closeQuietly(os, is);
		}
		connection.disconnect();
		Assert.assertEquals(payload, os.toString());
	}

	@Test
	public void makeRequest404Https() throws IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException {
		authenticationRequired = false;
		sslEnabled = true;
		startServer();
		configureSsl();
		final URL url = new URL("https://localhost:8888/qwertyuiop");
		final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setConnectTimeout(20000);
		connection.setReadTimeout(20000);
		Assert.assertEquals(404, connection.getResponseCode());
		connection.disconnect();
	}

	@Test
	public void makeRequests200HttpsAuth() throws IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException {
		authenticationRequired = true;
		sslEnabled = true;
		startServer();
		configureSsl();
		final URL url = new URL("https://localhost:8888" + HANDLER_PATH_TXT);
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setConnectTimeout(20000);
		connection.setReadTimeout(20000);
		Assert.assertEquals(401, connection.getResponseCode());
		Assert.assertEquals("Basic realm=\"" + REALM + '"', connection.getHeaderField("WWW-Authenticate"));
		connection.disconnect();
		connection = (HttpsURLConnection) url.openConnection();
		connection.setConnectTimeout(20000);
		connection.setReadTimeout(20000);
		connection.addRequestProperty("Authorization", "Basic " + CREDENTIALS_BASE64);
		Assert.assertEquals(200, connection.getResponseCode());
		Assert.assertEquals(payloadMd5, connection.getHeaderField("Etag"));
		Assert.assertTrue(connection.getContentType().startsWith("text/plain"));
		Assert.assertNotEquals(0, connection.getDate());
		InputStream is = null;
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			is = connection.getInputStream();
			IOUtils.copy(is, os, payload.length() / 3);
		}
		finally {
			IOUtils.closeQuietly(os, is);
		}
		connection.disconnect();
		Assert.assertEquals(payload, os.toString());
	}

	@Test
	public void makeRequests304HttpsAuth() throws IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException {
		authenticationRequired = true;
		sslEnabled = true;
		startServer();
		configureSsl();
		final URL url = new URL("https://localhost:8888" + HANDLER_PATH_TXT);
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setConnectTimeout(20000);
		connection.setReadTimeout(20000);
		Assert.assertEquals(401, connection.getResponseCode());
		Assert.assertEquals("Basic realm=\"" + REALM + '"', connection.getHeaderField("WWW-Authenticate"));
		connection.disconnect();
		connection = (HttpsURLConnection) url.openConnection();
		connection.setConnectTimeout(20000);
		connection.setReadTimeout(20000);
		connection.addRequestProperty("Authorization", "Basic " + CREDENTIALS_BASE64);
		connection.addRequestProperty("If-None-Match", payloadMd5);
		Assert.assertEquals(304, connection.getResponseCode());
		Assert.assertEquals(payloadMd5, connection.getHeaderField("Etag"));
		Assert.assertNotEquals(0, connection.getDate());
		InputStream is = null;
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			is = connection.getInputStream();
			IOUtils.copy(is, os, payload.length() / 3);
		}
		finally {
			IOUtils.closeQuietly(os, is);
		}
		connection.disconnect();
		Assert.assertTrue(os.toString().isEmpty());
	}

	private void configureSsl() throws NoSuchAlgorithmException, KeyManagementException {
		final SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, new TrustManager[] { new DummyTrustManager() }, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier(new DummyHostnameVerifier("localhost"));
	}

	private static void startServer() throws InterruptedException {
		server.start();

		final int retryPeriod = 100; // ms
		final int timeout = 5000; // ms
		int time = 0;
		do {
			try {
				TimeUnit.MILLISECONDS.sleep(time += retryPeriod);
			}
			catch (final InterruptedException e) {
				Thread.currentThread().interrupt();
				throw e;
			}
		}
		while (!server.isRunning() && time < timeout);
	}

	@After
	public void stopServer() {
		server.stop();
	}

	@AfterClass
	public static void destroy() {
		if (!certificate.delete()) {
			certificate.deleteOnExit();
		}
	}

}
