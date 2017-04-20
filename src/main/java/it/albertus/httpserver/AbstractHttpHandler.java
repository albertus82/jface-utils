package it.albertus.httpserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.GZIPOutputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.xml.bind.DatatypeConverter;

import org.apache.http.protocol.HttpDateGenerator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import it.albertus.httpserver.annotation.Path;
import it.albertus.jface.JFaceMessages;
import it.albertus.util.CRC32OutputStream;
import it.albertus.util.DigestOutputStream;
import it.albertus.util.IOUtils;
import it.albertus.util.NewLine;
import it.albertus.util.ResourceList;
import it.albertus.util.StringUtils;
import it.albertus.util.logging.LoggerFactory;

public abstract class AbstractHttpHandler implements HttpHandler {

	private static final Logger logger = LoggerFactory.getLogger(AbstractHttpHandler.class);

	private static final Map<Integer, String> httpStatusCodes;

	private static final Properties contentTypes;

	private static final Collection<String> resources;

	private static final ThreadLocal<MimetypesFileTypeMap> mimetypesFileTypeMap = new ThreadLocal<MimetypesFileTypeMap>() {
		@Override
		protected MimetypesFileTypeMap initialValue() {
			return new MimetypesFileTypeMap();
		}
	};

	static {
		final Properties properties = new Properties();
		InputStream is = null;
		try {
			is = AbstractHttpHandler.class.getResourceAsStream("http-codes.properties");
			properties.load(is);
		}
		catch (final IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			IOUtils.closeQuietly(is);
		}
		httpStatusCodes = new HashMap<Integer, String>(properties.size());
		for (final Entry<?, ?> entry : properties.entrySet()) {
			httpStatusCodes.put(Integer.valueOf(entry.getKey().toString()), entry.getValue().toString());
		}

		contentTypes = new Properties();
		try {
			is = AbstractHttpHandler.class.getResourceAsStream("mime-types.properties");
			contentTypes.load(is);
		}
		catch (final IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			IOUtils.closeQuietly(is);
		}

		resources = ResourceList.getResources(Pattern.compile(".*(?<!\\.class)$"));
		if (logger.isLoggable(Level.CONFIG)) {
			logger.config("HTTP static resources:");
			for (final String resource : resources) {
				logger.config(resource);
			}
		}
	}

	public static final String PREFERRED_CHARSET = "UTF-8";

	protected static final int BUFFER_SIZE = 4096;

	protected static final HttpDateGenerator httpDateGenerator = new HttpDateGenerator();

	private static final String MSG_KEY_BAD_METHOD = "msg.httpserver.bad.method";

	private static final Charset charset = initCharset();

	private static Object[] lastRequestInfo;

	private IHttpServerConfiguration httpServerConfiguration;

	private boolean enabled = true;

	private String path;

	@Override
	public void handle(final HttpExchange exchange) throws IOException {
		log(exchange);
		try {
			if (getHttpServerConfiguration().isEnabled() && isEnabled(exchange)) {
				service(exchange);
			}
			else {
				sendForbidden(exchange);
			}
		}
		catch (final HttpException e) {
			logger.log(Level.WARNING, e.toString(), e);
			sendError(exchange, e);
		}
		catch (final IOException e) {
			logger.log(Level.FINE, e.toString(), e); // often caused by the client that interrupts the stream.
		}
		catch (final Exception e) {
			logger.log(Level.SEVERE, e.toString(), e);
			sendInternalError(exchange);
		}
		finally {
			exchange.close();
		}
	}

	protected void service(final HttpExchange exchange) throws IOException, HttpException {
		if (HttpMethod.GET.equalsIgnoreCase(exchange.getRequestMethod())) {
			doGet(exchange);
		}
		else if (HttpMethod.POST.equalsIgnoreCase(exchange.getRequestMethod())) {
			doPost(exchange);
		}
		else if (HttpMethod.PUT.equalsIgnoreCase(exchange.getRequestMethod())) {
			doPut(exchange);
		}
		else if (HttpMethod.DELETE.equalsIgnoreCase(exchange.getRequestMethod())) {
			doDelete(exchange);
		}
		else if (HttpMethod.HEAD.equalsIgnoreCase(exchange.getRequestMethod())) {
			doHead(exchange);
		}
		else if (HttpMethod.TRACE.equalsIgnoreCase(exchange.getRequestMethod())) {
			doTrace(exchange);
		}
		else if (HttpMethod.OPTIONS.equalsIgnoreCase(exchange.getRequestMethod())) {
			doOptions(exchange);
		}
		else {
			throw new HttpException(HttpURLConnection.HTTP_BAD_METHOD, JFaceMessages.get(MSG_KEY_BAD_METHOD));
		}
	}

	protected void sendForbidden(final HttpExchange exchange) throws IOException {
		sendResponse(exchange, null, HttpURLConnection.HTTP_FORBIDDEN);
	}

	protected void sendInternalError(final HttpExchange exchange) throws IOException {
		sendResponse(exchange, null, HttpURLConnection.HTTP_INTERNAL_ERROR);
	}

	protected void sendError(final HttpExchange exchange, final HttpException e) throws IOException {
		sendResponse(exchange, null, e.getStatusCode());
	}

	protected void doHead(final HttpExchange exchange) throws IOException, HttpException {
		final OutputStream out = exchange.getResponseBody();
		final OutputStream dummy = new OutputStream() {
			@Override
			public void write(final int b) {/* Dummy */}
		};
		exchange.setStreams(exchange.getRequestBody(), dummy);
		try {
			doGet(exchange);
		}
		finally {
			try {
				out.close();
			}
			catch (final IOException e) {
				logger.log(Level.FINE, e.toString(), e);
			}
		}
	}

	protected void doTrace(final HttpExchange exchange) throws IOException, HttpException {
		final StringBuilder responseString = new StringBuilder(HttpMethod.TRACE.toUpperCase()).append(' ').append(exchange.getRequestURI()).append(' ').append(exchange.getProtocol());
		final Iterator<String> reqHeaderIter = exchange.getRequestHeaders().keySet().iterator();
		while (reqHeaderIter.hasNext()) {
			final String headerName = reqHeaderIter.next();
			responseString.append(NewLine.CRLF).append(headerName).append(": ").append(exchange.getRequestHeaders().getFirst(headerName));
		}
		responseString.append(NewLine.CRLF);

		exchange.getResponseHeaders().set("Content-Type", "message/http");
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, responseString.length());
		final OutputStream out = exchange.getResponseBody();
		out.write(responseString.toString().getBytes(getCharset()));
		out.close();
	}

	protected void doOptions(final HttpExchange exchange) throws IOException, HttpException {
		final Set<String> allowedMethods = new TreeSet<String>();
		allowedMethods.add(HttpMethod.TRACE.toUpperCase());
		allowedMethods.add(HttpMethod.OPTIONS.toUpperCase());
		for (final Method m : getAllDeclaredMethods(this.getClass())) {
			if ("doGet".equals(m.getName())) {
				allowedMethods.add(HttpMethod.GET.toUpperCase());
				allowedMethods.add(HttpMethod.HEAD.toUpperCase());
			}
			if ("doPost".equals(m.getName())) {
				allowedMethods.add(HttpMethod.POST.toUpperCase());
			}
			if ("doPut".equals(m.getName())) {
				allowedMethods.add(HttpMethod.PUT.toUpperCase());
			}
			if ("doDelete".equals(m.getName())) {
				allowedMethods.add(HttpMethod.DELETE.toUpperCase());
			}
		}

		final StringBuilder allow = new StringBuilder();
		for (final String allowedMethod : allowedMethods) {
			if (allow.length() != 0) {
				allow.append(", ");
			}
			allow.append(allowedMethod);
		}

		exchange.getResponseHeaders().add("Allow", allow.toString());
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
	}

	protected void doGet(final HttpExchange exchange) throws IOException, HttpException {
		throw new HttpException(HttpURLConnection.HTTP_BAD_METHOD, JFaceMessages.get(MSG_KEY_BAD_METHOD));
	}

	protected void doPost(final HttpExchange exchange) throws IOException, HttpException {
		throw new HttpException(HttpURLConnection.HTTP_BAD_METHOD, JFaceMessages.get(MSG_KEY_BAD_METHOD));
	}

	protected void doPut(final HttpExchange exchange) throws IOException, HttpException {
		throw new HttpException(HttpURLConnection.HTTP_BAD_METHOD, JFaceMessages.get(MSG_KEY_BAD_METHOD));
	}

	protected void doDelete(final HttpExchange exchange) throws IOException, HttpException {
		throw new HttpException(HttpURLConnection.HTTP_BAD_METHOD, JFaceMessages.get(MSG_KEY_BAD_METHOD));
	}

	private Method[] getAllDeclaredMethods(final Class<?> c) {
		if (c.equals(AbstractHttpHandler.class)) {
			return new Method[] {};
		}

		final Method[] parentMethods = getAllDeclaredMethods(c.getSuperclass());
		Method[] thisMethods = c.getDeclaredMethods();

		if (parentMethods.length > 0) {
			final Method[] allMethods = new Method[parentMethods.length + thisMethods.length];
			System.arraycopy(parentMethods, 0, allMethods, 0, parentMethods.length);
			System.arraycopy(thisMethods, 0, allMethods, parentMethods.length, thisMethods.length);
			thisMethods = allMethods;
		}

		return thisMethods;
	}

	private static Charset initCharset() {
		try {
			return Charset.forName(PREFERRED_CHARSET);
		}
		catch (final RuntimeException e) {
			logger.log(Level.WARNING, e.toString(), e);
			return Charset.defaultCharset();
		}
	}

	/**
	 * Adds {@code Content-Type} and {@code Date} headers to the provided
	 * {@link HttpExchange} object.
	 * 
	 * @param exchange the {@link HttpExchange} to be modified.
	 */
	protected void addCommonHeaders(final HttpExchange exchange) {
		addContentTypeHeader(exchange);
		addDateHeader(exchange);
	}

	protected void addContentTypeHeader(final HttpExchange exchange) {
		exchange.getResponseHeaders().add("Content-Type", getContentType(exchange.getRequestURI().getPath()));
	}

	protected String getContentType(final String fileName) {
		final String extension = fileName.indexOf('.') != -1 ? fileName.substring(fileName.lastIndexOf('.') + 1).trim().toLowerCase() : null;
		String contentType = null;
		if (extension != null && !extension.isEmpty()) {
			contentType = contentTypes.getProperty(extension);
		}
		if (contentType == null) {
			contentType = mimetypesFileTypeMap.get().getContentType(fileName);
		}
		return contentType.trim();
	}

	/**
	 * Adds {@code Date} header to the provided {@link HttpExchange} object.
	 * 
	 * @param exchange the {@link HttpExchange} to be modified.
	 */
	protected void addDateHeader(final HttpExchange exchange) {
		exchange.getResponseHeaders().add("Date", httpDateGenerator.getCurrentDate());
	}

	/**
	 * Adds {@code Content-Encoding: gzip} header to the provided
	 * {@link HttpExchange} object.
	 * 
	 * @param exchange the {@link HttpExchange} to be modified.
	 */
	protected void addGzipHeader(final HttpExchange exchange) {
		exchange.getResponseHeaders().add("Content-Encoding", "gzip");
	}

	protected void addEtagHeader(final HttpExchange exchange, final String eTag) {
		if (eTag != null) {
			exchange.getResponseHeaders().add("ETag", eTag);
		}
	}

	protected boolean canCompressResponse(final HttpExchange exchange) {
		if (getHttpServerConfiguration().isCompressionEnabled()) {
			final List<String> headers = exchange.getRequestHeaders().get("Accept-Encoding");
			if (headers != null) {
				for (final String header : headers) {
					if (header != null && header.trim().toLowerCase().contains("gzip")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	protected byte[] compressResponse(final byte[] uncompressed, final HttpExchange exchange) {
		if (canCompressResponse(exchange)) {
			try {
				return doCompressResponse(uncompressed, exchange);
			}
			catch (final IOException e) {
				logger.log(Level.WARNING, e.toString(), e);
			}
		}
		return uncompressed;
	}

	protected byte[] doCompressResponse(final byte[] uncompressed, final HttpExchange exchange) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(uncompressed.length / 4);
		GZIPOutputStream gzos = null;
		try {
			gzos = new GZIPOutputStream(baos);
			gzos.write(uncompressed);
		}
		finally {
			IOUtils.closeQuietly(gzos, baos);
		}
		addGzipHeader(exchange);
		return baos.toByteArray();
	}

	protected String generateEtag(final byte[] payload) {
		final CRC32 crc = new CRC32();
		crc.update(payload);
		return Long.toHexString(crc.getValue());
	}

	protected String generateEtag(final File file) throws IOException {
		final CRC32OutputStream os = new CRC32OutputStream();
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			IOUtils.copy(is, os, BUFFER_SIZE);
		}
		finally {
			IOUtils.closeQuietly(os, is);
		}
		return os.toString();
	}

	/*
	 * The MD5 digest is computed based on the content of the entity-body,
	 * including any content-coding that has been applied, but not including any
	 * transfer-encoding applied to the message-body. If the message is received
	 * with a transfer-encoding, that encoding MUST be removed prior to checking
	 * the Content-MD5 value against the received entity.
	 */
	protected String generateContentMd5(final File file) throws NoSuchAlgorithmException, IOException {
		FileInputStream fis = null;
		DigestOutputStream dos = null;
		try {
			fis = new FileInputStream(file);
			dos = new DigestOutputStream("MD5");
			IOUtils.copy(fis, dos, BUFFER_SIZE);
		}
		finally {
			IOUtils.closeQuietly(dos, fis);
		}
		return DatatypeConverter.printBase64Binary(dos.getValue());
	}

	protected String generateContentMd5(final byte[] responseBody) throws NoSuchAlgorithmException {
		final MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(responseBody);
		return DatatypeConverter.printBase64Binary(digest.digest());
	}

	protected void addContentMd5Header(final HttpExchange exchange, final File file) {
		try {
			exchange.getResponseHeaders().add("Content-MD5", generateContentMd5(file));
		}
		catch (final Exception e) {
			logger.log(Level.WARNING, e.toString(), e);
		}
	}

	protected void addContentMd5Header(final HttpExchange exchange, final byte[] responseBody) {
		try {
			exchange.getResponseHeaders().add("Content-MD5", generateContentMd5(responseBody));
		}
		catch (final Exception e) {
			logger.log(Level.WARNING, e.toString(), e);
		}
	}

	protected void sendResponse(final HttpExchange exchange, final byte[] payload, final int statusCode) throws IOException {
		final String currentEtag;
		if (statusCode >= HttpURLConnection.HTTP_OK && statusCode < HttpURLConnection.HTTP_MULT_CHOICE && payload != null) {
			currentEtag = generateEtag(payload);
			addEtagHeader(exchange, currentEtag);
		}
		else {
			currentEtag = null;
		}

		// If-None-Match...
		final String ifNoneMatch = exchange.getRequestHeaders().getFirst("If-None-Match");
		if (ifNoneMatch != null && currentEtag != null && currentEtag.equals(ifNoneMatch)) {
			addDateHeader(exchange);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_MODIFIED, -1);
			exchange.getResponseBody().close(); // Needed when no write occurs.
		}
		else {
			if (payload != null) {
				addCommonHeaders(exchange);
				final byte[] response = compressResponse(payload, exchange);
				if (HttpMethod.HEAD.equalsIgnoreCase(exchange.getRequestMethod())) {
					exchange.getResponseHeaders().set("Content-Length", Integer.toString(response.length));
					exchange.sendResponseHeaders(statusCode, -1);
					exchange.getResponseBody().close(); // no body
				}
				else {
					exchange.sendResponseHeaders(statusCode, response.length);
					exchange.getResponseBody().write(response);
				}
			}
			else { // no payload
				addDateHeader(exchange);
				exchange.sendResponseHeaders(statusCode, -1);
				exchange.getResponseBody().close();
			}
		}
	}

	protected void sendStaticResource(final HttpExchange exchange, final String resourceBasePath) throws IOException {
		final String pathInfo = StringUtils.substringAfter(exchange.getRequestURI().toString(), getPath());
		for (final String resource : resources) {
			if (('/' + resource.replace(File.separatorChar, '/')).endsWith(resourceBasePath + pathInfo)) {
				InputStream inputStream = null;
				ByteArrayOutputStream outputStream = null; // FIXME avoid ByteArrayOutputStream
				try {
					inputStream = getClass().getResourceAsStream(resourceBasePath + pathInfo);
					if (inputStream == null) {
						throw new IllegalStateException(resourceBasePath + pathInfo);
					}
					outputStream = new ByteArrayOutputStream();
					IOUtils.copy(inputStream, outputStream, BUFFER_SIZE);
				}
				finally {
					IOUtils.closeQuietly(outputStream, inputStream);
				}
				addCacheControlHeader(exchange);
				sendResponse(exchange, outputStream.toByteArray());
			}
		}
		sendNotFound(exchange);
		return;
	}

	protected void addCacheControlHeader(final HttpExchange exchange) {
		exchange.getResponseHeaders().add("Cache-Control", "no-transform,public,max-age=86400,s-maxage=259200");
	}

	protected void sendNotFound(final HttpExchange exchange) throws IOException {
		sendResponse(exchange, null, HttpURLConnection.HTTP_NOT_FOUND);
	}

	protected void sendResponse(final HttpExchange exchange, final byte[] payload) throws IOException {
		sendResponse(exchange, payload, HttpURLConnection.HTTP_OK);
	}

	protected void log(final HttpExchange exchange) {
		Level level = Level.OFF;
		try {
			level = Level.parse(httpServerConfiguration.getRequestLoggingLevel());
		}
		catch (final RuntimeException e) {
			logger.log(Level.WARNING, e.toString(), e);
		}
		doLog(exchange, level);
	}

	protected void doLog(final HttpExchange exchange, final Level level) {
		if (logger.isLoggable(level) && !Level.OFF.equals(level)) {
			final Object[] requestInfo = new Object[] { exchange.getRemoteAddress(), exchange.getRequestMethod(), exchange.getRequestURI() };
			if (!Arrays.equals(requestInfo, getLastRequestInfo())) {
				setLastRequestInfo(requestInfo);
				logger.log(level, JFaceMessages.get("msg.httpserver.log.request"), new Object[] { Thread.currentThread().getName(), exchange.getRemoteAddress(), exchange.getRequestMethod(), exchange.getRequestURI() });
			}
		}
	}

	protected Charset getCharset() {
		return charset;
	}

	/**
	 * Returns if this handler is enabled. <b>Handlers are enabled by
	 * default.</b> Requests to disabled handlers will be bounced with <b>HTTP
	 * Status-Code 403: Forbidden.</b> Calling this method is equivalent to
	 * invoke the overloaded version without arguments: {@code isEnabled()}. You
	 * are allowed to override this method to take any other kind of decision.
	 * 
	 * @param exchange the current {@link HttpExchange} object.
	 * 
	 * @return {@code true} if this handler is enabled, otherwise {@code false}.
	 */
	public boolean isEnabled(final HttpExchange exchange) {
		return isEnabled();
	}

	/**
	 * Returns if this handler is enabled. <b>Handlers are enabled by
	 * default.</b> Requests to disabled handlers will be bounced with <b>HTTP
	 * Status-Code 403: Forbidden.</b>
	 * 
	 * @return {@code true} if this handler is enabled, otherwise {@code false}.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public String getPath() {
		return path != null ? path : getPath(this.getClass());
	}

	protected void setPath(final String path) {
		this.path = path;
	}

	public static String getPath(final Class<? extends HttpHandler> clazz) {
		final Path annotation = clazz.getAnnotation(Path.class);
		return annotation != null ? annotation.value() : null;
	}

	protected static Object[] getLastRequestInfo() {
		return lastRequestInfo;
	}

	protected static void setLastRequestInfo(final Object[] requestInfo) {
		lastRequestInfo = requestInfo;
	}

	protected IHttpServerConfiguration getHttpServerConfiguration() {
		return httpServerConfiguration;
	}

	void setHttpServerConfiguration(final IHttpServerConfiguration httpServerConfiguration) {
		this.httpServerConfiguration = httpServerConfiguration;
	}

	public static Map<Integer, String> getHttpStatusCodes() {
		return httpStatusCodes;
	}

	public static Properties getContentTypes() {
		return contentTypes;
	}

	public static Collection<String> getResources() {
		return resources;
	}

}
