package it.albertus.net.httpserver.filter;

import java.io.IOException;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings("restriction")
public class HSTSResponseFilter extends Filter {

	private static final String HEADER_KEY = "Strict-Transport-Security";

	private final String headerValue;

	public HSTSResponseFilter(final int maxAge, final boolean includeSubDomains, final boolean preload) {
		final StringBuilder value = new StringBuilder("max-age=").append(maxAge);
		if (includeSubDomains) {
			value.append("; includeSubDomains");
		}
		if (preload) {
			value.append("; preload");
		}
		this.headerValue = value.toString();
	}

	public HSTSResponseFilter(final String headerValue) {
		this.headerValue = headerValue;
	}

	@Override
	public void doFilter(final HttpExchange exchange, final Chain chain) throws IOException {
		exchange.getResponseHeaders().set(HEADER_KEY, headerValue);
		chain.doFilter(exchange);
	}

	@Override
	public String description() {
		return getClass().getSimpleName();
	}

}
