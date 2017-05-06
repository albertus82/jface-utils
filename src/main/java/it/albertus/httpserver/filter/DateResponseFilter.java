package it.albertus.httpserver.filter;

import java.io.IOException;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import it.albertus.httpserver.HttpDateGenerator;

public class DateResponseFilter extends Filter {

	private static final ThreadLocal<HttpDateGenerator> httpDateGenerator = new ThreadLocal<HttpDateGenerator>() {
		@Override
		protected HttpDateGenerator initialValue() {
			return new HttpDateGenerator();
		}
	};

	@Override
	public void doFilter(final HttpExchange exchange, final Chain chain) throws IOException {
		exchange.getResponseHeaders().set("Date", httpDateGenerator.get().getCurrentDate());
		chain.doFilter(exchange);
	}

	@Override
	public String description() {
		return getClass().getSimpleName();
	}

}
