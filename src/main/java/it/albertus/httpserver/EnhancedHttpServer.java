package it.albertus.httpserver;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
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

public class EnhancedHttpServer {

	private static final Logger logger = LoggerFactory.getLogger(EnhancedHttpServer.class);

	protected static final int STOP_DELAY = 0;

	protected final IHttpServerConfiguration httpServerConfiguration;

	protected volatile HttpServer httpServer;
	protected volatile boolean running = false;
	protected volatile ThreadPoolExecutor threadPool;

	private final Object lock = new Object();

	public EnhancedHttpServer(final IHttpServerConfiguration httpServerConfiguration) {
		this.httpServerConfiguration = httpServerConfiguration;
	}

	public void start() {
		if (!running && httpServerConfiguration.isEnabled()) {
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
		if (httpServerConfiguration.isAuthenticationRequired()) {
			try {
				final Configured<String> username = new Configured<String>() {
					@Override
					public String getValue() {
						return httpServerConfiguration.getUsername();
					}
				};
				final Configured<char[]> password = new Configured<char[]>() {
					@Override
					public char[] getValue() {
						return httpServerConfiguration.getPassword();
					}
				};
				authenticator = new HttpServerAuthenticator(httpServerConfiguration.getRealm(), username, password, httpServerConfiguration.getPasswordHashAlgorithm());
			}
			catch (final NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}
		else {
			authenticator = null;
		}

		for (final AbstractHttpHandler handler : createHandlers()) {
			handler.setHttpServerConfiguration(httpServerConfiguration); // Injection
			final HttpContext httpContext = httpServer.createContext(handler.getPath(), handler);
			if (authenticator != null) {
				httpContext.setAuthenticator(authenticator);
			}
		}
	}

	/**
	 * Creates {@code HttpHandler} objects.
	 * 
	 * @return the array containing the handlers.
	 */
	protected AbstractHttpHandler[] createHandlers() {
		return httpServerConfiguration.getHandlers();
	}

	protected class HttpServerStartThread extends Thread {

		public HttpServerStartThread() {
			this.setName("httpServerStartThread");
			this.setDaemon(true);
		}

		@Override
		public void run() {
			final int port = httpServerConfiguration.getPort();
			final InetSocketAddress address = new InetSocketAddress(port);
			try {
				synchronized (lock) {
					// Avoid server starvation
					System.setProperty("sun.net.httpserver.maxReqTime", Long.toString(httpServerConfiguration.getMaxReqTime()));
					System.setProperty("sun.net.httpserver.maxRspTime", Long.toString(httpServerConfiguration.getMaxRspTime()));

					if (httpServerConfiguration.isSslEnabled()) {
						final char[] storepass = httpServerConfiguration.getStorePass();
						final KeyStore keyStore = KeyStore.getInstance(httpServerConfiguration.getKeyStoreType());
						// keytool -genkey -alias "myalias" -keyalg "RSA" -keypass "mykeypass" -keystore "mykeystore.jks" -storepass "mystorepass" -validity 360
						FileInputStream fis = null;
						BufferedInputStream bis = null;
						try {
							fis = new FileInputStream(httpServerConfiguration.getKeyStoreFileName());
							bis = new BufferedInputStream(fis);
							keyStore.load(bis, storepass);
						}
						finally {
							IOUtils.closeQuietly(bis, fis);
						}

						final char[] keypass = httpServerConfiguration.getKeyPass();
						final KeyManagerFactory kmf = KeyManagerFactory.getInstance(httpServerConfiguration.getKeyManagerFactoryAlgorithm());
						kmf.init(keyStore, keypass);

						final TrustManagerFactory tmf = TrustManagerFactory.getInstance(httpServerConfiguration.getTrustManagerFactoryAlgorithm());
						tmf.init(keyStore);

						final SSLContext sslContext = SSLContext.getInstance(httpServerConfiguration.getSslProtocol());
						sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

						final SSLParameters sslParameters = httpServerConfiguration.getSslParameters(sslContext);
						logger.log(Level.CONFIG, "SSLParameters [cipherSuites={0}, protocols={1}, wantClientAuth={2}, needClientAuth={3}]", new Object[] { Arrays.toString(sslParameters.getCipherSuites()), Arrays.toString(sslParameters.getProtocols()), sslParameters.getWantClientAuth(), sslParameters.getNeedClientAuth() });

						final HttpsConfigurator httpsConfigurator = new HttpsConfigurator(sslContext) {
							@Override
							public void configure(final HttpsParameters params) {
								params.setSSLParameters(sslParameters);
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

					final int maximumPoolSize = httpServerConfiguration.getMaxThreadCount();
					if (maximumPoolSize > 1) {
						threadPool = new ThreadPoolExecutor(httpServerConfiguration.getMinThreadCount(), maximumPoolSize, httpServerConfiguration.getThreadKeepAliveTime(), TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new DaemonThreadFactory());
						threadPool.setRejectedExecutionHandler(new RejectedExecutionHandler() {
							@Override
							public void rejectedExecution(final Runnable r, final ThreadPoolExecutor executor) {
								try {
									executor.getQueue().put(r);
								}
								catch (final InterruptedException e) {
									logger.log(Level.FINE, e.toString(), e);
									Thread.currentThread().interrupt();
								}
							}
						});
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
