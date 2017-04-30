package it.albertus.httpserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.net.httpserver.HttpExchange;

import it.albertus.util.IOUtils;

public class LightweightHttpServerTest {

	private static final int PORT = 8888;
	private static final String USERNAME = "test";
	// private static final String PASSWORD = "TESTtest12345";
	private static final String PASSWORD_HASH = "92f1a57051141e9d24396bc42ae43b6500d13f8b";
	private static final String REALM = "Test Realm";
	private static final String CREDENTIALS_BASE64 = "dGVzdDpURVNUdGVzdDEyMzQ1";

	private static final String HANDLER_NAME_LOREM = "/loremIpsum.txt";
	private static final String HANDLER_NAME_DISABLED = "/disabled";

	private static final String payload = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
	private static final String payloadMd5 = "db89bb5ceab87f9c0fcc2ab36c189c2c";

	private static boolean authenticationRequired = false;

	private static LightweightHttpServer server;

	@BeforeClass
	public static void init() throws InterruptedException {
		final IHttpServerConfiguration configuration = new DefaultHttpServerConfiguration() {

			@Override
			public AbstractHttpHandler[] getHandlers() {
				final AbstractHttpHandler handler1 = new AbstractHttpHandler() {
					@Override
					protected void doGet(final HttpExchange exchange) throws IOException, HttpException {
						sendResponse(exchange, payload.getBytes());
					}
				};
				handler1.setPath(HANDLER_NAME_LOREM);
				final AbstractHttpHandler handler2 = new AbstractHttpHandler() {
					@Override
					protected void doGet(final HttpExchange exchange) throws IOException, HttpException {
						sendResponse(exchange, payload.getBytes());
					}
				};
				handler2.setEnabled(false);
				handler2.setPath(HANDLER_NAME_DISABLED);
				return new AbstractHttpHandler[] { handler1, handler2 };
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
		};
		server = new LightweightHttpServer(configuration);
	}

	@Test
	public void makeRequest200() throws IOException, InterruptedException {
		authenticationRequired = false;
		startServer();
		final URL url = new URL("http://localhost:8888" + HANDLER_NAME_LOREM);
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		Assert.assertEquals(200, connection.getResponseCode());
		Assert.assertEquals(payloadMd5, connection.getHeaderField("Etag"));
		Assert.assertTrue(connection.getHeaderField("Content-Type").startsWith("text/plain"));
		Assert.assertNotNull(connection.getHeaderField("Date"));
		InputStream is = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
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
		startServer();
		final URL url = new URL("http://localhost:8888" + HANDLER_NAME_LOREM);
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.addRequestProperty("If-None-Match", payloadMd5);
		Assert.assertEquals(304, connection.getResponseCode());
		Assert.assertNotNull(connection.getHeaderField("Date"));
		Assert.assertEquals(payloadMd5, connection.getHeaderField("Etag"));
		InputStream is = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
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
		startServer();
		final URL url = new URL("http://localhost:8888" + HANDLER_NAME_LOREM);
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		Assert.assertEquals(401, connection.getResponseCode());
		connection.disconnect();
	}

	@Test
	public void makeRequests200Auth() throws IOException, InterruptedException {
		authenticationRequired = true;
		startServer();
		final URL url = new URL("http://localhost:8888" + HANDLER_NAME_LOREM);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		Assert.assertEquals(401, connection.getResponseCode());
		Assert.assertEquals("Basic realm=\"" + REALM + '"', connection.getHeaderField("WWW-Authenticate"));
		connection.disconnect();
		connection = (HttpURLConnection) url.openConnection();
		connection.addRequestProperty("Authorization", "Basic " + CREDENTIALS_BASE64);
		Assert.assertEquals(200, connection.getResponseCode());
		Assert.assertEquals(payloadMd5, connection.getHeaderField("Etag"));
		Assert.assertTrue(connection.getHeaderField("Content-Type").startsWith("text/plain"));
		Assert.assertNotNull(connection.getHeaderField("Date"));
		InputStream is = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
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
	public void makeRequests403() throws IOException, InterruptedException {
		authenticationRequired = false;
		startServer();
		final URL url = new URL("http://localhost:8888" + HANDLER_NAME_DISABLED);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		Assert.assertEquals(403, connection.getResponseCode());
		Assert.assertNotNull(connection.getHeaderField("Date"));
		connection.disconnect();
	}

	@Test
	public void makeRequests404() throws IOException, InterruptedException {
		authenticationRequired = false;
		startServer();
		final URL url = new URL("http://localhost:8888" + "/qwertyuiop");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		Assert.assertEquals(404, connection.getResponseCode());
		connection.disconnect();
	}

	@Test
	public void makeRequests304Auth() throws IOException, InterruptedException {
		authenticationRequired = true;
		startServer();
		final URL url = new URL("http://localhost:8888" + HANDLER_NAME_LOREM);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		Assert.assertEquals(401, connection.getResponseCode());
		Assert.assertEquals("Basic realm=\"" + REALM + '"', connection.getHeaderField("WWW-Authenticate"));
		connection.disconnect();
		connection = (HttpURLConnection) url.openConnection();
		connection.addRequestProperty("Authorization", "Basic " + CREDENTIALS_BASE64);
		connection.addRequestProperty("If-None-Match", payloadMd5);
		Assert.assertEquals(304, connection.getResponseCode());
		Assert.assertNotNull(connection.getHeaderField("Date"));
		Assert.assertEquals(payloadMd5, connection.getHeaderField("Etag"));
		InputStream is = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
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

	private static void startServer() throws InterruptedException {
		server.start();

		final int retryPeriod = 100; // ms
		final int timeout = 5000; // ms
		int time = 0;
		while (!server.isRunning() && time < timeout) {
			try {
				TimeUnit.MILLISECONDS.sleep(time += retryPeriod);
			}
			catch (final InterruptedException e) {
				Thread.currentThread().interrupt();
				throw e;
			}
		}
	}

	@After
	public void stopServer() {
		server.stop();
	}

}
