package it.albertus.httpserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.Date;
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
import java.util.zip.GZIPOutputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.xml.bind.DatatypeConverter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import it.albertus.httpserver.annotation.Path;
import it.albertus.httpserver.config.IHttpServerConfig;
import it.albertus.jface.JFaceMessages;
import it.albertus.util.ClasspathResourceUtils;
import it.albertus.util.DigestOutputStream;
import it.albertus.util.IOUtils;
import it.albertus.util.NewLine;
import it.albertus.util.Resource;
import it.albertus.util.StringUtils;
import it.albertus.util.logging.LoggerFactory;

@SuppressWarnings("restriction")
public abstract class AbstractHttpHandler implements HttpPathHandler {

	private static final Logger logger = LoggerFactory.getLogger(AbstractHttpHandler.class);

	private static final Map<Integer, String> httpStatusCodes = initHttpStatusCodes();

	private static final Properties contentTypes = initContentTypes();

	private static Collection<Resource> resources; // Lazy initialization (may be huge)

	private static final Charset charset = initCharset();

	private static final ThreadLocal<HttpDateGenerator> httpDateGenerator = new ThreadLocal<HttpDateGenerator>() {
		@Override
		protected HttpDateGenerator initialValue() {
			return new HttpDateGenerator();
		}
	};

	public static final String PREFERRED_CHARSET = "UTF-8";

	protected static final int BUFFER_SIZE = 8192; // 8 KiB

	private static final String MSG_HTTPSERVER_BAD_METHOD = "msg.httpserver.bad.method";

	private static Object[] lastRequestInfo;

	private static final ThreadLocal<MessageDigest> md5Digest = new ThreadLocal<MessageDigest>() {
		@Override
		protected MessageDigest initialValue() {
			try {
				return MessageDigest.getInstance("MD5");
			}
			catch (final NoSuchAlgorithmException e) {
				throw new IllegalStateException(e);
			}
		}

		@Override
		public MessageDigest get() {
			final MessageDigest md = super.get();
			md.reset();
			return md;
		}
	};

	private final IHttpServerConfig httpServerConfig;

	private boolean enabled = true;

	private String path;

	private static Map<Integer, String> initHttpStatusCodes() {
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
		final Map<Integer, String> httpStatusCodes = new HashMap<Integer, String>((int) (properties.size() / 0.75) + 1);
		for (final Entry<?, ?> entry : properties.entrySet()) {
			httpStatusCodes.put(Integer.valueOf(entry.getKey().toString()), entry.getValue().toString());
		}
		return httpStatusCodes;
	}

	private static Properties initContentTypes() {
		final Properties contentTypes = new Properties();
		InputStream is = null;
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
		return contentTypes;
	}

	private static Collection<Resource> initResources() {
		final Collection<Resource> resources = ClasspathResourceUtils.getResourceList(Pattern.compile(".*(?<!\\.class)$"));
		logger.log(Level.CONFIG, JFaceMessages.get("msg.httpserver.resources.found"), Integer.toString(resources.size()));
		if (logger.isLoggable(Level.FINE)) {
			for (final Resource resource : resources) {
				logger.fine(String.valueOf(resource));
			}
		}
		return resources;
	}

	public AbstractHttpHandler(final IHttpServerConfig config) {
		this.httpServerConfig = config;
	}

	@Override
	public void handle(final HttpExchange exchange) throws IOException {
		log(exchange);
		try {
			if (httpServerConfig.isEnabled() && isEnabled(exchange)) {
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
		else if (HttpMethod.PATCH.equalsIgnoreCase(exchange.getRequestMethod())) {
			doPatch(exchange);
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
			throw new HttpException(HttpURLConnection.HTTP_BAD_METHOD, JFaceMessages.get(MSG_HTTPSERVER_BAD_METHOD));
		}
	}

	protected void sendForbidden(final HttpExchange exchange) throws IOException {
		sendResponse(exchange, HttpURLConnection.HTTP_FORBIDDEN);
	}

	protected void sendInternalError(final HttpExchange exchange) throws IOException {
		sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR);
	}

	protected void sendError(final HttpExchange exchange, final HttpException e) throws IOException {
		sendResponse(exchange, e.getStatusCode());
	}

	protected void doHead(final HttpExchange exchange) throws IOException, HttpException {
		final OutputStream out = exchange.getResponseBody();
		final OutputStream dummy = new OutputStream() {
			@Override
			public void write(final int b) {/* Dummy */}
		};
		exchange.setStreams(null, dummy);
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

		setContentTypeHeader(exchange, "message/http");
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

		exchange.getResponseHeaders().set("Allow", allow.toString());
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
	}

	protected void doGet(final HttpExchange exchange) throws IOException, HttpException {
		throw new HttpException(HttpURLConnection.HTTP_BAD_METHOD, JFaceMessages.get(MSG_HTTPSERVER_BAD_METHOD));
	}

	protected void doPost(final HttpExchange exchange) throws IOException, HttpException {
		throw new HttpException(HttpURLConnection.HTTP_BAD_METHOD, JFaceMessages.get(MSG_HTTPSERVER_BAD_METHOD));
	}

	protected void doPut(final HttpExchange exchange) throws IOException, HttpException {
		throw new HttpException(HttpURLConnection.HTTP_BAD_METHOD, JFaceMessages.get(MSG_HTTPSERVER_BAD_METHOD));
	}

	protected void doPatch(final HttpExchange exchange) throws IOException, HttpException {
		throw new HttpException(HttpURLConnection.HTTP_BAD_METHOD, JFaceMessages.get(MSG_HTTPSERVER_BAD_METHOD));
	}

	protected void doDelete(final HttpExchange exchange) throws IOException, HttpException {
		throw new HttpException(HttpURLConnection.HTTP_BAD_METHOD, JFaceMessages.get(MSG_HTTPSERVER_BAD_METHOD));
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

	protected void setContentTypeHeader(final HttpExchange exchange) {
		setContentTypeHeader(exchange, getContentType(exchange.getRequestURI().getPath()));
	}

	protected void setContentTypeHeader(final HttpExchange exchange, final String value) {
		if (value != null && !value.isEmpty()) {
			exchange.getResponseHeaders().set("Content-Type", value);
		}
		else {
			exchange.getResponseHeaders().remove("Content-Type");
		}
	}

	protected String getContentType(final String fileName) {
		final String extension = fileName.indexOf('.') != -1 ? fileName.substring(fileName.lastIndexOf('.') + 1).trim().toLowerCase() : null;
		String contentType = null;
		if (extension != null && !extension.isEmpty()) {
			contentType = getContentTypes().getProperty(extension);
		}
		if (contentType == null) {
			contentType = new MimetypesFileTypeMap().getContentType(fileName);
		}
		return contentType.trim();
	}

	/**
	 * Adds {@code Content-Encoding: gzip} header to the provided
	 * {@link HttpExchange} object.
	 * 
	 * @param exchange the {@link HttpExchange} to be modified.
	 */
	protected void setGzipHeader(final HttpExchange exchange) {
		exchange.getResponseHeaders().set("Content-Encoding", "gzip");
	}

	protected void setEtagHeader(final HttpExchange exchange, final String eTag) {
		if (eTag != null && (HttpMethod.GET.equalsIgnoreCase(exchange.getRequestMethod()) || HttpMethod.HEAD.equalsIgnoreCase(exchange.getRequestMethod()))) {
			exchange.getResponseHeaders().set("ETag", eTag);
		}
		else {
			exchange.getResponseHeaders().remove("ETag");
		}
	}

	protected void setLastModifiedHeader(final HttpExchange exchange, final Date lastModified) {
		if (lastModified != null && (HttpMethod.GET.equalsIgnoreCase(exchange.getRequestMethod()) || HttpMethod.HEAD.equalsIgnoreCase(exchange.getRequestMethod()))) {
			exchange.getResponseHeaders().set("Last-Modified", getHttpDateGenerator().format(lastModified));
		}
		else {
			exchange.getResponseHeaders().remove("Last-Modified");
		}
	}

	protected void setRefreshHeader(final HttpExchange exchange, final Integer seconds) {
		if (seconds != null) {
			exchange.getResponseHeaders().set("Refresh", seconds.toString());
		}
		else {
			exchange.getResponseHeaders().remove("Refresh");
		}
	}

	protected boolean canCompressResponse(final HttpExchange exchange) {
		if (httpServerConfig.isCompressionEnabled()) {
			final List<String> headerRows = exchange.getRequestHeaders().get("Accept-Encoding");
			if (headerRows != null) {
				for (final String headerRow : headerRows) {
					if (headerRow != null) {
						for (final String headerValue : headerRow.split(",")) {
							if ("gzip".equalsIgnoreCase(headerValue.trim())) {
								return true;
							}
						}
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
		final byte[] compressed = baos.toByteArray();
		if (compressed.length < uncompressed.length) {
			setGzipHeader(exchange);
			return compressed;
		}
		else {
			return uncompressed;
		}
	}

	protected String generateEtag(final byte[] payload) {
		return DatatypeConverter.printHexBinary(md5Digest.get().digest(payload)).toLowerCase();
	}

	protected String generateEtag(final File file) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			return generateEtag(is);
		}
		finally {
			IOUtils.closeQuietly(is);
		}
	}

	protected String generateEtag(final InputStream inputStream) throws IOException {
		final OutputStream os = new DigestOutputStream(md5Digest.get());
		try {
			IOUtils.copy(inputStream, os, BUFFER_SIZE);
		}
		finally {
			IOUtils.closeQuietly(os);
		}
		return os.toString();
	}

	protected String generateContentMd5(final byte[] responseBody) {
		return DatatypeConverter.printBase64Binary(md5Digest.get().digest(responseBody));
	}

	/*
	 * The MD5 digest is computed based on the content of the entity-body,
	 * including any content-coding that has been applied, but not including any
	 * transfer-encoding applied to the message-body. If the message is received
	 * with a transfer-encoding, that encoding MUST be removed prior to checking
	 * the Content-MD5 value against the received entity.
	 */
	protected String generateContentMd5(final File file) throws IOException {
		InputStream is = null;
		final DigestOutputStream os = new DigestOutputStream(md5Digest.get());
		try {
			is = new FileInputStream(file);
			IOUtils.copy(is, os, BUFFER_SIZE);
		}
		finally {
			IOUtils.closeQuietly(os, is);
		}
		return DatatypeConverter.printBase64Binary(os.getValue());
	}

	protected void setContentMd5Header(final HttpExchange exchange, final File file) {
		try {
			exchange.getResponseHeaders().set("Content-MD5", generateContentMd5(file));
		}
		catch (final Exception e) {
			logger.log(Level.WARNING, e.toString(), e);
		}
	}

	protected void setContentMd5Header(final HttpExchange exchange, final byte[] responseBody) {
		try {
			exchange.getResponseHeaders().set("Content-MD5", generateContentMd5(responseBody));
		}
		catch (final Exception e) {
			logger.log(Level.WARNING, e.toString(), e);
		}
	}

	protected void sendResponse(final HttpExchange exchange, final int statusCode) throws IOException {
		sendResponse(exchange, null, statusCode);
	}

	protected void setStatusHeader(final HttpExchange exchange, final int statusCode) {
		final String description = getHttpStatusCodes().get(statusCode);
		if (description != null) {
			exchange.getResponseHeaders().set("Status", statusCode + " " + description);
		}
	}

	protected void sendResponse(final HttpExchange exchange, final byte[] payload, final int statusCode) throws IOException {
		final String method = exchange.getRequestMethod();
		final String currentEtag;
		if (payload != null && statusCode >= HttpURLConnection.HTTP_OK && statusCode < HttpURLConnection.HTTP_MULT_CHOICE && (HttpMethod.GET.equalsIgnoreCase(method) || HttpMethod.HEAD.equalsIgnoreCase(method))) {
			currentEtag = generateEtag(payload);
			setEtagHeader(exchange, currentEtag);
		}
		else {
			currentEtag = null;
		}

		// If-None-Match...
		final String ifNoneMatch = exchange.getRequestHeaders().getFirst("If-None-Match");
		if (ifNoneMatch != null && currentEtag != null && currentEtag.equals(ifNoneMatch)) {
			setStatusHeader(exchange, HttpURLConnection.HTTP_NOT_MODIFIED);
			setContentDispositionHeader(exchange, null);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_MODIFIED, -1);
		}
		else {
			setStatusHeader(exchange, statusCode);
			if (payload != null) {
				setContentTypeHeader(exchange);
				final byte[] response = compressResponse(payload, exchange);
				if (HttpMethod.HEAD.equalsIgnoreCase(method)) {
					exchange.getResponseHeaders().set("Content-Length", Integer.toString(response.length));
					exchange.sendResponseHeaders(statusCode, -1);
				}
				else {
					exchange.sendResponseHeaders(statusCode, response.length);
					exchange.getResponseBody().write(response);
				}
			}
			else { // no payload
				exchange.sendResponseHeaders(statusCode, -1);
			}
		}
		exchange.getResponseBody().close();
	}

	protected Resource getStaticResource(final String resourcePath) {
		for (final Resource resource : getResources()) {
			if (('/' + resource.getName().replace(File.separatorChar, '/')).endsWith(resourcePath)) {
				return resource;
			}
		}
		return null;
	}

	protected String getPathInfo(final HttpExchange exchange) {
		return StringUtils.substringBefore(StringUtils.substringAfter(exchange.getRequestURI().getPath(), getPath()), "?");
	}

	protected void sendStaticResource(final HttpExchange exchange, String resourcePath, final boolean attachment, final String cacheControl) throws IOException {
		if (!resourcePath.startsWith("/")) {
			resourcePath = '/' + resourcePath;
		}
		final Resource resource = getStaticResource(resourcePath);
		if (resource != null) {
			doSendStaticResource(exchange, resourcePath, resource, attachment, cacheControl);
		}
		else {
			sendNotFound(exchange);
		}
	}

	private void doSendStaticResource(final HttpExchange exchange, final String resourcePath, final Resource resource, final boolean attachment, final String cacheControl) throws IOException {
		final String method = exchange.getRequestMethod();
		final String fileName = new File(resource.getName()).getName();
		final long fileSize = resource.getSize();
		InputStream inputStream = null;
		try {
			if (fileSize >= 0 && fileSize < httpServerConfig.getResponseBufferLimit()) {
				// In memory for small files
				inputStream = getClass().getResourceAsStream(resourcePath);
				if (inputStream == null) {
					throw new IllegalStateException(resourcePath);
				}
				sendStaticInMemoryResponse(exchange, inputStream, attachment ? fileName : null, cacheControl);
			}
			else {
				// Streaming for large files
				final String currentEtag;
				if (HttpMethod.GET.equalsIgnoreCase(method) || HttpMethod.HEAD.equalsIgnoreCase(method)) {
					inputStream = getClass().getResourceAsStream(resourcePath);
					if (inputStream == null) {
						throw new IllegalStateException(resourcePath);
					}
					currentEtag = generateEtag(inputStream);
					IOUtils.closeQuietly(inputStream);
					setEtagHeader(exchange, currentEtag);
				}
				else {
					currentEtag = null;
				}

				// If-None-Match...
				final String ifNoneMatch = exchange.getRequestHeaders().getFirst("If-None-Match");
				if (ifNoneMatch != null && currentEtag != null && currentEtag.equals(ifNoneMatch)) {
					sendStaticNotModifiedResponse(exchange, cacheControl);
				}
				else {
					setStaticHeaders(exchange, attachment ? fileName : null, cacheControl);

					OutputStream output = null;
					try {
						output = prepareStaticOutputStream(exchange, fileSize);
						if (!HttpMethod.HEAD.equalsIgnoreCase(exchange.getRequestMethod())) {
							inputStream = getClass().getResourceAsStream(resourcePath);
							if (inputStream == null) {
								throw new IllegalStateException(resourcePath);
							}
							IOUtils.copy(inputStream, output, BUFFER_SIZE);
						}
					}
					finally {
						IOUtils.closeQuietly(output);
					}
				}
			}
		}
		finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	private void sendStaticInMemoryResponse(final HttpExchange exchange, InputStream inputStream, final String fileName, final String cacheControl) throws IOException {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		IOUtils.copy(inputStream, outputStream, BUFFER_SIZE);
		IOUtils.closeQuietly(inputStream);
		setCacheControlHeader(exchange, cacheControl);
		if (fileName != null) {
			setContentDispositionHeader(exchange, "attachment; filename=\"" + fileName + "\"");
		}
		sendResponse(exchange, outputStream.toByteArray());
	}

	private OutputStream prepareStaticOutputStream(final HttpExchange exchange, final long fileSize) throws IOException {
		OutputStream output = null;
		if (canCompressResponse(exchange)) {
			setGzipHeader(exchange);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0); // Transfer-Encoding: chunked
			if (!HttpMethod.HEAD.equalsIgnoreCase(exchange.getRequestMethod())) {
				output = new GZIPOutputStream(exchange.getResponseBody(), BUFFER_SIZE);
			}
		}
		else {
			if (!HttpMethod.HEAD.equalsIgnoreCase(exchange.getRequestMethod())) {
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, fileSize);
				output = exchange.getResponseBody();
			}
			else {
				exchange.getResponseHeaders().set("Content-Length", Long.toString(fileSize));
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
			}
		}
		return output;
	}

	protected void sendStaticFile(final HttpExchange exchange, final String basePath, final String pathInfo, final boolean attachment, final String cacheControl) throws IOException {
		final File file = new File(basePath + pathInfo);
		if (file.getCanonicalPath().startsWith(new File(basePath).getCanonicalPath())) {
			doSendStaticFile(exchange, file, attachment, cacheControl);
		}
		else {
			sendNotFound(exchange);
			logger.log(Level.WARNING, JFaceMessages.get("err.httpserver.traversal", file, exchange.getRemoteAddress()));
		}
	}

	private void doSendStaticFile(final HttpExchange exchange, final File file, final boolean attachment, final String cacheControl) throws IOException {
		final String method = exchange.getRequestMethod();
		final String fileName = file.getName();
		final long fileSize = file.length();
		InputStream inputStream = null;
		try {
			if (fileSize >= 0 && fileSize < httpServerConfig.getResponseBufferLimit()) {
				inputStream = new FileInputStream(file);
				sendStaticInMemoryResponse(exchange, inputStream, attachment ? fileName : null, cacheControl);
			}
			else {
				// Streaming for large files
				final String currentEtag;
				if (HttpMethod.GET.equalsIgnoreCase(method) || HttpMethod.HEAD.equalsIgnoreCase(method)) {
					inputStream = new FileInputStream(file);
					currentEtag = generateEtag(inputStream);
					IOUtils.closeQuietly(inputStream);
					setEtagHeader(exchange, currentEtag);
				}
				else {
					currentEtag = null;
				}

				// If-None-Match...
				final String ifNoneMatch = exchange.getRequestHeaders().getFirst("If-None-Match");
				if (ifNoneMatch != null && currentEtag != null && currentEtag.equals(ifNoneMatch)) {
					sendStaticNotModifiedResponse(exchange, cacheControl);
				}
				else {
					setStaticHeaders(exchange, attachment ? fileName : null, cacheControl);

					OutputStream output = null;
					try {
						output = prepareStaticOutputStream(exchange, fileSize);
						if (!HttpMethod.HEAD.equalsIgnoreCase(method)) {
							inputStream = new FileInputStream(file);
							IOUtils.copy(inputStream, output, BUFFER_SIZE);
						}
					}
					finally {
						IOUtils.closeQuietly(output);
					}
				}
			}
		}
		catch (final FileNotFoundException e) {
			logger.log(Level.FINE, e.toString(), e);
			sendNotFound(exchange);
		}
		finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	private void sendStaticNotModifiedResponse(final HttpExchange exchange, final String cacheControl) throws IOException {
		setStatusHeader(exchange, HttpURLConnection.HTTP_NOT_MODIFIED);
		setCacheControlHeader(exchange, cacheControl);
		setContentDispositionHeader(exchange, null);
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_MODIFIED, -1);
	}

	private void setStaticHeaders(final HttpExchange exchange, final String fileName, final String cacheControl) {
		setContentTypeHeader(exchange);
		setCacheControlHeader(exchange, cacheControl);
		setStatusHeader(exchange, HttpURLConnection.HTTP_OK);
		if (fileName != null) {
			setContentDispositionHeader(exchange, "attachment; filename=\"" + fileName + "\"");
		}
	}

	protected void setContentDispositionHeader(final HttpExchange exchange, final String value) {
		if (value != null && !value.isEmpty()) {
			exchange.getResponseHeaders().set("Content-Disposition", value);
		}
		else {
			exchange.getResponseHeaders().remove("Content-Disposition");
		}
	}

	protected void setCacheControlHeader(final HttpExchange exchange, final String value) {
		if (value != null && !value.isEmpty()) {
			exchange.getResponseHeaders().set("Cache-Control", value);
		}
		else {
			exchange.getResponseHeaders().remove("Cache-Control");
		}
	}

	protected void sendNotFound(final HttpExchange exchange) throws IOException {
		sendResponse(exchange, HttpURLConnection.HTTP_NOT_FOUND);
	}

	protected void sendResponse(final HttpExchange exchange, final byte[] payload) throws IOException {
		sendResponse(exchange, payload, HttpURLConnection.HTTP_OK);
	}

	protected void log(final HttpExchange exchange) {
		Level level = Level.OFF;
		try {
			level = Level.parse(httpServerConfig.getRequestLoggingLevel());
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

	@Override
	public String getPath() {
		return path != null ? path : getAnnotatedPath(getClass());
	}

	protected void setPath(final String path) {
		this.path = path;
	}

	protected IHttpServerConfig getHttpServerConfig() {
		return httpServerConfig;
	}

	public static String getAnnotatedPath(final Class<? extends HttpHandler> clazz) {
		final Path annotation = clazz.getAnnotation(Path.class);
		return annotation != null ? annotation.value() : null;
	}

	protected static Object[] getLastRequestInfo() {
		return lastRequestInfo;
	}

	protected static void setLastRequestInfo(final Object[] requestInfo) {
		lastRequestInfo = requestInfo;
	}

	public static Map<Integer, String> getHttpStatusCodes() {
		return httpStatusCodes;
	}

	public static Properties getContentTypes() {
		return contentTypes;
	}

	public static synchronized Collection<Resource> getResources() {
		if (resources == null) {
			resources = initResources();
		}
		return resources;
	}

	public static HttpDateGenerator getHttpDateGenerator() {
		return httpDateGenerator.get();
	}

}
