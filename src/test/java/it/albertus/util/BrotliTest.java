package it.albertus.util;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class BrotliTest {

	private static final String CHARSET_NAME = "UTF-8";

	private static final String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean a scelerisque tortor. Fusce vehicula tellus quam, dictum ornare dolor placerat sed. Cras varius vulputate lorem, eu dignissim dolor imperdiet ut. Sed rutrum nisi eu metus porta semper. Etiam pharetra mauris dolor, vel mattis arcu rhoncus quis. Interdum et malesuada fames ac ante ipsum primis in faucibus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Praesent non facilisis nulla. Curabitur placerat, quam vitae semper ultrices, augue dui scelerisque nunc, sagittis viverra sem nisi quis elit. Nunc rhoncus efficitur est. Nullam non libero et augue ultricies commodo. Fusce eleifend in risus at sagittis. Praesent nec nunc sollicitudin libero varius tempor. Phasellus interdum et est nec cursus.";

	@Test
	public void testCompress() throws IOException {
		final byte[] uncompressed = loremIpsum.getBytes(CHARSET_NAME);

		final Brotli brotli = new Brotli();

		final byte[] compressed = brotli.compress(uncompressed);
		final byte[] decompressed = brotli.decompress(compressed);

		Assert.assertArrayEquals(uncompressed, decompressed);
		Assert.assertEquals(loremIpsum, new String(decompressed, CHARSET_NAME));
	}

}
