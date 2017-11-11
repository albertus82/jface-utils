package it.albertus.net.httpserver.filter;

import java.io.IOException;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import it.albertus.util.ISupplier;

/**
 * This filter adds a HTTP {@code Strict-Transport-Security} response header to
 * every HTTP response.
 */
@SuppressWarnings("restriction")
public class HSTSResponseFilter extends Filter {

	private static final String HEADER_KEY = "Strict-Transport-Security";

	private ISupplier<Integer> maxAge;
	private ISupplier<Boolean> includeSubDomains;
	private ISupplier<Boolean> preload;

	private String headerValue;

	/**
	 * Creates the filter using the provided suppliers for the generation of the
	 * {@code Strict-Transport-Security} header value.
	 * 
	 * @param maxAge the supplier of the {@code max-age} directive value
	 * @param includeSubDomains the supplier of the {@code includeSubDomains}
	 *        option switch (the option is omitted when the supplier returns
	 *        {@code false})
	 * @param preload the supplier of the {@code preload} option switch (the
	 *        option is omitted when the supplier returns {@code false})
	 */
	public HSTSResponseFilter(final ISupplier<Integer> maxAge, final ISupplier<Boolean> includeSubDomains, final ISupplier<Boolean> preload) {
		this.maxAge = maxAge;
		this.includeSubDomains = includeSubDomains;
		this.preload = preload;
	}

	/**
	 * Creates the filter using the provided values for the generation of the
	 * {@code Strict-Transport-Security} header value.
	 * 
	 * @param maxAge the {@code max-age} directive value
	 * @param includeSubDomains the {@code includeSubDomains} option switch (the
	 *        option is omitted when the supplier returns {@code false})
	 * @param preload the {@code preload} option switch (the option is omitted
	 *        when the supplier returns {@code false})
	 */
	public HSTSResponseFilter(final int maxAge, final boolean includeSubDomains, final boolean preload) {
		this(new ISupplier<Integer>() {
			@Override
			public Integer get() {
				return maxAge;
			}
		}, new ISupplier<Boolean>() {
			@Override
			public Boolean get() {
				return includeSubDomains;
			}
		}, new ISupplier<Boolean>() {
			@Override
			public Boolean get() {
				return preload;
			}
		});
	}

	/**
	 * Creates the filter using the provided value for the
	 * {@code Strict-Transport-Security} header.
	 * 
	 * @param headerValue the value that will be used for the
	 *        {@code Strict-Transport-Security} header.
	 */
	public HSTSResponseFilter(final String headerValue) {
		this.headerValue = headerValue;
	}

	@Override
	public void doFilter(final HttpExchange exchange, final Chain chain) throws IOException {
		exchange.getResponseHeaders().set(HEADER_KEY, getHeaderValue());
		chain.doFilter(exchange);
	}

	private String getHeaderValue() {
		if (headerValue != null) {
			return headerValue;
		}
		else {
			final StringBuilder value = new StringBuilder("max-age=").append(maxAge.get().intValue());
			if (includeSubDomains.get()) {
				value.append("; includeSubDomains");
			}
			if (preload.get()) {
				value.append("; preload");
			}
			return value.toString();
		}
	}

	@Override
	public String description() {
		return getClass().getSimpleName();
	}

}
