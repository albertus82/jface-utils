package it.albertus.httpserver;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

import it.albertus.jface.JFaceMessages;
import it.albertus.util.Configured;
import it.albertus.util.DaemonThreadFactory;
import it.albertus.util.IOUtils;
import it.albertus.util.logging.LoggerFactory;

public abstract class AbstractHttpServer {

	private static final Logger logger = LoggerFactory.getLogger(AbstractHttpServer.class);

	protected static final int STOP_DELAY = 0;

	protected final IHttpServerConfiguration configuration;

	protected volatile HttpServer httpServer;
	protected volatile boolean running = false;
	protected volatile ExecutorService threadPool;

	private final Object lock = new Object();

	public AbstractHttpServer(final IHttpServerConfiguration configuration) {
		this.configuration = configuration;
	}

	public void start() {
		if (!running && configuration.isEnabled()) {
			new HttpServerStartThread().start();
		}
	}

	public void stop() {
		if (httpServer != null) {
			synchronized (lock) {
				try {
					httpServer.stop(STOP_DELAY);
					if (threadPool != null && !threadPool.isShutdown()) {
						try {
							threadPool.shutdown();
						}
						catch (final Exception e) {
							logger.log(Level.SEVERE, e.toString(), e);
						}
					}
				}
				catch (final Exception e) {
					logger.log(Level.SEVERE, e.toString(), e);
				}
				running = false;
			}
		}
	}

	public boolean isRunning() {
		return running;
	}

	protected void createContexts() {
		final Authenticator authenticator;
		if (configuration.isAuthenticationRequired()) {
			try {
				final Configured<String> username = new Configured<String>() {
					@Override
					public String getValue() {
						return configuration.getUsername();
					}
				};
				final Configured<char[]> password = new Configured<char[]>() {
					@Override
					public char[] getValue() {
						return configuration.getPassword();
					}
				};
				authenticator = new HttpServerAuthenticator(configuration.getRealm(), username, password, configuration.getPasswordHashAlgorithm());
			}
			catch (final NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}
		else {
			authenticator = null;
		}

		for (final AbstractHttpHandler handler : createHandlers()) {
			final HttpContext httpContext = httpServer.createContext(handler.getPath(), handler);
			if (authenticator != null) {
				httpContext.setAuthenticator(authenticator);
			}
		}
	}

	/**
	 * Creates {@code HttpHandler} objects.
	 * 
	 * @return the {@code Set} containing the handlers.
	 */
	protected abstract Set<AbstractHttpHandler> createHandlers();

	protected class HttpServerStartThread extends Thread {

		public HttpServerStartThread() {
			this.setName("httpServerStartThread");
			this.setDaemon(true);
		}

		@Override
		public void run() {
			final int port = configuration.getPort();
			final InetSocketAddress address = new InetSocketAddress(port);
			try {
				synchronized (lock) {
					// Avoid server starvation
					System.setProperty("sun.net.httpserver.maxReqTime", Short.toString(configuration.getMaxReqTime()));
					System.setProperty("sun.net.httpserver.maxRspTime", Short.toString(configuration.getMaxRspTime()));

					if (configuration.isSslEnabled()) {
						final char[] storepass = configuration.getStorePass();
						final KeyStore keyStore = KeyStore.getInstance(configuration.getKeyStoreType());
						// keytool -genkey -alias "myalias" -keyalg "RSA" -keypass "mykeypass" -keystore "mykeystore.jks" -storepass "mystorepass" -validity 360
						FileInputStream fis = null;
						BufferedInputStream bis = null;
						try {
							fis = new FileInputStream(configuration.getKeyStoreFileName());
							bis = new BufferedInputStream(fis);
							keyStore.load(bis, storepass);
						}
						finally {
							IOUtils.closeQuietly(bis, fis);
						}

						final char[] keypass = configuration.getKeyPass();
						final KeyManagerFactory kmf = KeyManagerFactory.getInstance(configuration.getKeyManagerFactoryAlgorithm());
						kmf.init(keyStore, keypass);

						final TrustManagerFactory tmf = TrustManagerFactory.getInstance(configuration.getTrustManagerFactoryAlgorithm());
						tmf.init(keyStore);

						final SSLContext sslContext = SSLContext.getInstance(configuration.getSslProtocol());
						sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
						final HttpsConfigurator httpsConfigurator = new HttpsConfigurator(sslContext) {
							@Override
							public void configure(final HttpsParameters params) {
								try {
									final SSLEngine sslEngine = getSSLContext().createSSLEngine();
									params.setNeedClientAuth(false);
									params.setCipherSuites(sslEngine.getEnabledCipherSuites());
									params.setProtocols(sslEngine.getEnabledProtocols());

									final SSLParameters defaultSSLParameters = getSSLContext().getDefaultSSLParameters();
									params.setSSLParameters(defaultSSLParameters);
								}
								catch (final Exception e) {
									logger.log(Level.SEVERE, e.toString(), e);
								}
							}
						};

						final HttpsServer httpsServer = HttpsServer.create(address, 0);
						httpsServer.setHttpsConfigurator(httpsConfigurator);
						httpServer = httpsServer;
					}
					else {
						httpServer = HttpServer.create(address, 0);
					}
					createContexts();

					final byte threads = configuration.getThreadCount();
					if (threads > 1) {
						threadPool = Executors.newFixedThreadPool(threads, new DaemonThreadFactory());
						httpServer.setExecutor(threadPool);
					}

					httpServer.start();
					running = true;
				}
			}
			catch (final BindException e) {
				logger.log(Level.SEVERE, JFaceMessages.get("err.httpserver.start.port", port), e);
			}
			catch (final FileNotFoundException e) {
				logger.log(Level.SEVERE, JFaceMessages.get("err.httpserver.start.keystore.file"), e);
			}
			catch (final Exception e) {
				logger.log(Level.SEVERE, JFaceMessages.get("err.httpserver.start"), e);
			}
		}
	}

}
