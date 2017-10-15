package it.albertus.util.version;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Test;

public class LatestReleaseTest {

	@Test
	public void test() throws IOException {
		final String json = new LatestReleaseChecker().check(new GitHubLatestReleaseUrlSupplier("albertus82", "jfaceutils"), null, new LatestReleaseCallback() {
			@Override
			public String parse(final byte[] response) {
				return new String(response, Charset.forName("UTF-8"));
			}
		});
		Assert.assertNotNull(json);
	}

}
