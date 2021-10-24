package io.github.albertus82.net.httpserver.filter;

import java.io.IOException;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import io.github.albertus82.net.httpserver.HttpDateGenerator;

/**
 * This filter adds a HTTP {@code Date} response header to every HTTP response.
 */
@SuppressWarnings("restriction")
public class DateResponseFilter extends Filter {

	@Override
	public void doFilter(final HttpExchange exchange, final Chain chain) throws IOException {
		exchange.getResponseHeaders().set("Date", new HttpDateGenerator().getCurrentDate());
		chain.doFilter(exchange);
	}

	@Override
	public String description() {
		return getClass().getSimpleName();
	}

}
