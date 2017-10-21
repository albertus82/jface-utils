package it.albertus.net.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;

import it.albertus.util.logging.LoggerFactory;

@SuppressWarnings("restriction")
public class RequestParameterExtractor {

	private static final Logger logger = LoggerFactory.getLogger(RequestParameterExtractor.class);

	public static final String PREFERRED_CHARSET = "UTF-8";

	private final Map<String, String[]> parameterMap = new HashMap<String, String[]>();

	public RequestParameterExtractor(final HttpExchange exchange, final Charset charset) throws IOException {
		final Map<String, List<String>> params = new HashMap<String, List<String>>();
		params.putAll(parseQueryParameters(exchange, charset));
		if (HttpMethod.POST.equalsIgnoreCase(exchange.getRequestMethod()) || HttpMethod.PUT.equalsIgnoreCase(exchange.getRequestMethod()) || HttpMethod.DELETE.equalsIgnoreCase(exchange.getRequestMethod()) || HttpMethod.PATCH.equalsIgnoreCase(exchange.getRequestMethod())) {
			params.putAll(parseBodyParameters(exchange, charset));
		}
		for (final Entry<String, List<String>> entry : params.entrySet()) {
			parameterMap.put(entry.getKey(), entry.getValue().toArray(new String[entry.getValue().size()]));
		}
	}

	public RequestParameterExtractor(final HttpExchange exchange, final String charsetName) throws IOException {
		this(exchange, Charset.forName(charsetName));
	}

	public RequestParameterExtractor(final HttpExchange exchange) throws IOException {
		this(exchange, initCharset());
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

	public Map<String, String[]> getParameterMap() {
		return Collections.unmodifiableMap(parameterMap);
	}

	public String[] getParameterValues(final String name) {
		return parameterMap.get(name);
	}

	public String getParameter(final String name) {
		return parameterMap.containsKey(name) ? parameterMap.get(name)[0] : null;
	}

	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(getParameterMap().keySet());
	}

	private Map<String, List<String>> parseQueryParameters(final HttpExchange exchange, final Charset charset) throws UnsupportedEncodingException {
		final URI requestedUri = exchange.getRequestURI();
		final String query = requestedUri.getRawQuery();
		return parseQuery(query, charset);
	}

	private Map<String, List<String>> parseBodyParameters(final HttpExchange exchange, final Charset charset) throws IOException {
		final InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), charset);
		final BufferedReader br = new BufferedReader(isr);
		final String query = br.readLine();
		return parseQuery(query, charset);
	}

	private Map<String, List<String>> parseQuery(final String query, final Charset charset) throws UnsupportedEncodingException {
		final Map<String, List<String>> map = new HashMap<String, List<String>>();
		if (query != null) {
			final String[] pairs = query.split("[&]");

			for (final String pair : pairs) {
				final String[] param = pair.split("[=]");

				String key = null;
				String value = null;
				if (param.length > 0) {
					key = URLDecoder.decode(param[0], charset.name());
				}

				if (param.length > 1) {
					value = URLDecoder.decode(param[1], charset.name());
				}

				if (!map.containsKey(key)) {
					map.put(key, new ArrayList<String>(1));
				}
				map.get(key).add(value);
			}
		}
		return map;
	}

}
