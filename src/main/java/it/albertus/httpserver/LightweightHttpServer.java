package it.albertus.httpserver;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

import it.albertus.httpserver.config.IHttpServerConfig;
import it.albertus.jface.JFaceMessages;
import it.albertus.util.DaemonThreadFactory;
import it.albertus.util.IOUtils;
import it.albertus.util.logging.LoggerFactory;

@SuppressWarnings("restriction")
public class LightweightHttpServer {

	private static final Logger logger = LoggerFactory.getLogger(LightweightHttpServer.class);

	protected final IHttpServerConfig httpServerConfiguration;

	private volatile HttpServer server;
	protected volatile boolean running = false;
	protected volatile ThreadPoolExecutor threadPool;

	private Collection<HttpContext> contexts;

	private final Object lock = new Object();

	public LightweightHttpServer(final IHttpServerConfig httpServerConfiguration) {
		this.httpServerConfiguration = httpServerConfiguration;
	}

	/**
	 * Starts this server as a daemon.
	 */
	public void start() {
		start(true);
	}

	/**
	 * Starts this server.
	 * 
	 * @param daemon determine if the server thread will be configured as a
	 *        deamon or not.
	 * 
	 */
	public void start(final boolean daemon) {
		if (!running && httpServerConfiguration.isEnabled()) {
			new HttpServerStartThread(daemon).start();
		}
	}

	/**
	 * Stops this server by closing the listening socket and disallowing any new
	 * exchanges from being processed. The method will then block until all
	 * current exchange handlers have completed or else when approximately
	 * <i>delay</i> seconds have elapsed (whichever happens sooner). Then, all
	 * open TCP connections are closed, the background thread created by start()
	 * exits, the thread pool is shutdown and the method returns.
	 * <p>
	 *
	 * @param delay the maximum time in seconds to wait until exchanges have
	 *        finished.
	 * @throws IllegalArgumentException if delay is less than zero.
	 */
	public void stop(final int delay) {
		if (server != null) {
			synchronized (lock) {
				try {
					server.stop(delay);
					shutdownThreadPool();
				}
				catch (final Exception e) {
					logger.log(Level.SEVERE, e.toString(), e);
				}
				running = false;
			}
		}
	}

	/**
	 * Stops this server by closing the listening socket and disallowing any new
	 * exchanges from being processed. The method will then block until all
	 * current exchange handlers have completed or else when approximately the
	 * number of seconds returned by {@link IHttpServerConfig#getStopDelay()}
	 * has elapsed (whichever happens sooner). Then, all open TCP connections
	 * are closed, the background thread created by start() exits, the thread
	 * pool is shutdown and the method returns.
	 * <p>
	 *
	 * @throws IllegalArgumentException if delay is less than zero.
	 * @see LightweightHttpServer#stop(int)
	 * @see IHttpServerConfig#getStopDelay()
	 */
	public void stop() {
		stop(httpServerConfiguration.getStopDelay());
	}

	private void shutdownThreadPool() {
		if (threadPool != null && !threadPool.isShutdown()) {
			try {
				threadPool.shutdown();
			}
			catch (final Exception e) {
				logger.log(Level.SEVERE, e.toString(), e);
			}
		}
	}

	/**
	 * Returns whether the server is currently running or not.
	 * 
	 * @return {@code true} if the server is running, otherwise {@code false}.
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @return the currently active {@link HttpServer} object or null if the
	 *         server was never started.
	 */
	public HttpServer getServer() {
		return server;
	}

	/**
	 * @return the currently configured {@link HttpContext} objects or null if
	 *         the server was never started.
	 */
	public Collection<HttpContext> getContexts() {
		return contexts;
	}

	protected Collection<HttpContext> createContexts() {
		final Filter[] filtersArray = createFilters();
		final Collection<Filter> filtersList;
		if (filtersArray != null && filtersArray.length != 0) {
			filtersList = Arrays.asList(filtersArray);
		}
		else {
			filtersList = Collections.emptyList();
		}

		final Collection<HttpContext> httpContexts = new ArrayList<HttpContext>();
		final Authenticator authenticator = httpServerConfiguration.getAuthenticator();
		final HttpPathHandler[] handlersArray = createHandlers();
		if (handlersArray != null && handlersArray.length > 0) {
			for (final HttpPathHandler handler : handlersArray) {
				final HttpContext httpContext = server.createContext(handler.getPath(), handler);
				if (filtersArray != null && filtersArray.length != 0) {
					httpContext.getFilters().addAll(filtersList);
				}
				if (authenticator != null) {
					httpContext.setAuthenticator(authenticator);
				}
				httpContexts.add(httpContext);
			}
		}
		else {
			logger.log(Level.WARNING, JFaceMessages.get("msg.httpserver.configuration.handlers.none"));
		}
		return httpContexts;
	}

	/**
	 * Creates {@link AbstractHttpHandler} objects.
	 * 
	 * @return the array containing the handlers.
	 */
	protected HttpPathHandler[] createHandlers() {
		return httpServerConfiguration.getHandlers();
	}

	/**
	 * Creates {@link com.sun.net.httpserver.Filter Filter} objects.
	 * 
	 * @return the array containing the filters.
	 */
	protected Filter[] createFilters() {
		return httpServerConfiguration.getFilters();
	}

	protected class HttpServerStartThread extends Thread {

		public HttpServerStartThread(final boolean daemon) {
			this.setName("httpServerStartThread");
			this.setDaemon(daemon);
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
						server = httpsServer;
					}
					else {
						server = HttpServer.create(address, 0);
					}
					contexts = createContexts();

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
						server.setExecutor(threadPool);
					}

					server.start();
					running = true;
					logger.log(Level.INFO, JFaceMessages.get("msg.httpserver.started", port));
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
