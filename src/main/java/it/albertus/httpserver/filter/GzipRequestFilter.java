package it.albertus.httpserver.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

public class GzipRequestFilter extends Filter {

	@Override
	public void doFilter(final HttpExchange exchange, final Chain chain) throws IOException {
		if ("gzip".equalsIgnoreCase(exchange.getRequestHeaders().getFirst("Content-Encoding"))) {
			final InputStream in = exchange.getRequestBody();
			if (in != null) {
				exchange.setStreams(new GZIPInputStream(in), null);
			}
		}
		chain.doFilter(exchange);
	}

	@Override
	public String description() {
		return getClass().getSimpleName();
	}

}
