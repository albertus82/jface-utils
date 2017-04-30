package it.albertus.httpserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.net.httpserver.HttpExchange;

import it.albertus.util.IOUtils;

public class LightweightHttpServerTest {

	private static final int PORT = 8888;

	private static final String HANDLER_NAME = "/handlerName.txt";

	private static final String payload = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

	private static LightweightHttpServer server;

	@BeforeClass
	public static void init() {
		final IHttpServerConfiguration configuration = new DefaultHttpServerConfiguration() {
			@Override
			public AbstractHttpHandler[] getHandlers() {
				final AbstractHttpHandler handler = new AbstractHttpHandler() {
					@Override
					protected void doGet(final HttpExchange exchange) throws IOException, HttpException {
						sendResponse(exchange, payload.getBytes());
					}
				};
				handler.setPath(HANDLER_NAME);
				return new AbstractHttpHandler[] { handler };
			}

			@Override
			public int getPort() {
				return PORT;
			}
		};
		server = new LightweightHttpServer(configuration);
		server.start();
	}

	@Test
	public void test() throws IOException {
		final URL url = new URL("http://localhost:8888" + HANDLER_NAME);
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.getResponseCode();
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

	@AfterClass
	public static void destroy() throws InterruptedException {
		server.stop();
	}

}
