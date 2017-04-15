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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.GZIPOutputStream;

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
import it.albertus.util.logging.LoggerFactory;

public abstract class AbstractHttpHandler implements HttpHandler {

	private static final Logger logger = LoggerFactory.getLogger(AbstractHttpHandler.class);

	private static final Map<Integer, String> httpStatusCodes;

	static {
		httpStatusCodes = new HashMap<Integer, String>();
		httpStatusCodes.put(100, "Continue");
		httpStatusCodes.put(101, "Switching Protocols");
		httpStatusCodes.put(102, "Processing");
		httpStatusCodes.put(200, "OK");
		httpStatusCodes.put(201, "Created");
		httpStatusCodes.put(202, "Accepted");
		httpStatusCodes.put(203, "Non-Authoritative Information");
		httpStatusCodes.put(204, "No Content");
		httpStatusCodes.put(205, "Reset Content");
		httpStatusCodes.put(206, "Partial Content");
		httpStatusCodes.put(207, "Multi-Status");
		httpStatusCodes.put(208, "Already Reported");
		httpStatusCodes.put(226, "IM Used");
		httpStatusCodes.put(300, "Multiple Choices");
		httpStatusCodes.put(301, "Moved Permanently");
		httpStatusCodes.put(302, "Found");
		httpStatusCodes.put(303, "See Other");
		httpStatusCodes.put(304, "Not Modified");
		httpStatusCodes.put(305, "Use Proxy");
		httpStatusCodes.put(307, "Temporary Redirect");
		httpStatusCodes.put(308, "Permanent Redirect");
		httpStatusCodes.put(400, "Bad Request");
		httpStatusCodes.put(401, "Unauthorized");
		httpStatusCodes.put(402, "Payment Required");
		httpStatusCodes.put(403, "Forbidden");
		httpStatusCodes.put(404, "Not Found");
		httpStatusCodes.put(405, "Method Not Allowed");
		httpStatusCodes.put(406, "Not Acceptable");
		httpStatusCodes.put(407, "Proxy Authentication Required");
		httpStatusCodes.put(408, "Request Timeout");
		httpStatusCodes.put(409, "Conflict");
		httpStatusCodes.put(410, "Gone");
		httpStatusCodes.put(411, "Length Required");
		httpStatusCodes.put(412, "Precondition Failed");
		httpStatusCodes.put(413, "Request Entity Too Large");
		httpStatusCodes.put(414, "Request-URI Too Long");
		httpStatusCodes.put(415, "Unsupported Media Type");
		httpStatusCodes.put(416, "Requested Range Not Satisfiable");
		httpStatusCodes.put(417, "Expectation Failed");
		httpStatusCodes.put(418, "I'm a teapot");
		httpStatusCodes.put(421, "Misdirected Request");
		httpStatusCodes.put(422, "Unprocessable Entity");
		httpStatusCodes.put(423, "Locked");
		httpStatusCodes.put(424, "Failed Dependency");
		httpStatusCodes.put(426, "Upgrade Required");
		httpStatusCodes.put(428, "Precondition Required");
		httpStatusCodes.put(429, "Too Many Requests");
		httpStatusCodes.put(431, "Request Header Fields Too Large");
		httpStatusCodes.put(451, "Unavailable For Legal Reasons");
		httpStatusCodes.put(500, "Internal Server Error");
		httpStatusCodes.put(501, "Not Implemented");
		httpStatusCodes.put(502, "Bad Gateway");
		httpStatusCodes.put(503, "Service Unavailable");
		httpStatusCodes.put(504, "Gateway Timeout");
		httpStatusCodes.put(505, "HTTP Version Not Supported");
		httpStatusCodes.put(506, "Variant Also Negotiates");
		httpStatusCodes.put(507, "Insufficient Storage");
		httpStatusCodes.put(508, "Loop Detected");
		httpStatusCodes.put(510, "Not Extended");
		httpStatusCodes.put(511, "Network Authentication Required");
	}

	public static final String PREFERRED_CHARSET = "UTF-8";

	protected static final HttpDateGenerator httpDateGenerator = new HttpDateGenerator();

	private static final String MSG_KEY_BAD_METHOD = "msg.httpserver.bad.method";

	private static final int BUFFER_SIZE = 4096;

	private static final Charset charset = initCharset();

	private static Object[] lastRequestInfo;

	protected final IHttpServerConfiguration httpServerConfiguration;

	public AbstractHttpHandler(final IHttpServerConfiguration httpServerConfiguration) {
		this.httpServerConfiguration = httpServerConfiguration;
	}

	@Override
	public void handle(final HttpExchange exchange) throws IOException {
		service(exchange);
	}

	protected final void service(final HttpExchange exchange) throws IOException, HttpException {
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

	protected abstract void addContentTypeHeader(HttpExchange exchange);

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
		final List<String> headers = exchange.getRequestHeaders().get("Accept-Encoding");
		if (headers != null) {
			for (final String header : headers) {
				if (header != null && header.trim().toLowerCase().contains("gzip")) {
					return true;
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
		if (statusCode >= HttpURLConnection.HTTP_OK && statusCode < HttpURLConnection.HTTP_MULT_CHOICE) {
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
	 * Status-Code 403: Forbidden.</b>
	 * 
	 * @return {@code true} if this handler is enabled, otherwise {@code false}.
	 */
	public boolean isEnabled() {
		return true;
	}

	public static Map<Integer, String> getHttpStatusCodes() {
		return Collections.unmodifiableMap(httpStatusCodes);
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

}
