package it.albertus.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import it.albertus.util.logging.LoggerFactory;

public class BrotliAdapterTest {

	private static final Logger logger = LoggerFactory.getLogger(BrotliAdapterTest.class);

	private static final int JOBS_COUNT = 50;
	private static final int CONCURRENT_THREADS = 4;

	private static final String CHARSET_NAME = "UTF-8";

	// @formatter:off
	private static final String[] testStrings = {
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean a scelerisque tortor. Fusce vehicula tellus quam, dictum ornare dolor placerat sed. Cras varius vulputate lorem, eu dignissim dolor imperdiet ut. Sed rutrum nisi eu metus porta semper. Etiam pharetra mauris dolor, vel mattis arcu rhoncus quis. Interdum et malesuada fames ac ante ipsum primis in faucibus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Praesent non facilisis nulla. Curabitur placerat, quam vitae semper ultrices, augue dui scelerisque nunc, sagittis viverra sem nisi quis elit. Nunc rhoncus efficitur est. Nullam non libero et augue ultricies commodo. Fusce eleifend in risus at sagittis. Praesent nec nunc sollicitudin libero varius tempor. Phasellus interdum et est nec cursus.",
			"Quisque nec ultrices eros, eu interdum mi. Pellentesque lorem elit, placerat id nisi non, posuere vulputate libero. Duis tincidunt tortor ligula, non rhoncus urna vestibulum vitae. Vestibulum posuere magna ut nunc molestie dignissim. Donec nec mollis nibh. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Nunc tincidunt malesuada elit. Phasellus sollicitudin consectetur malesuada. Maecenas convallis leo sit amet justo tincidunt finibus.",
			"Etiam consectetur pellentesque imperdiet. Pellentesque urna justo, tincidunt a risus in, mollis consectetur nunc. Ut eu sem congue, tincidunt est gravida, congue risus. Vestibulum lobortis nisl non placerat dictum. Sed erat orci, molestie id nunc non, commodo placerat sapien. Aenean luctus fringilla dolor, sed tempus nibh consectetur vitae. Ut in auctor eros, sit amet malesuada est. Vestibulum leo ante, vulputate sed ornare a, dictum nec est. Suspendisse quis sodales felis. Aenean ac nisi quis est pulvinar tincidunt. Etiam commodo egestas ante in pellentesque. Duis euismod urna et felis accumsan tempus. Ut ultricies nisl sit amet mauris finibus sodales. Proin semper sed enim a tincidunt. Aenean euismod ante massa. Donec nec nisl nisl.",
			"Donec in consectetur nisl, sed euismod erat. Maecenas mattis laoreet hendrerit. In commodo nunc mi, ac efficitur lacus luctus vel. Pellentesque nulla justo, consequat eget finibus in, pharetra sit amet sapien. In at augue libero. Sed suscipit, ipsum vitae sagittis interdum, libero erat dictum massa, ut tempus urna mauris a risus. Cras ultricies mauris vel sem pulvinar, vel faucibus felis mattis. Phasellus quis posuere risus. Donec ligula odio, pharetra et nulla eget, vestibulum pharetra tortor. Sed lacinia nibh tincidunt velit interdum tempus. In vel malesuada dolor, id rhoncus libero. Cras felis mauris, accumsan vel rhoncus quis, blandit vitae sem. Fusce porta faucibus ex, at lacinia nibh imperdiet ac." };
	// @formatter:on

	private static BrotliAdapter instance;

	@BeforeClass
	public static void init() {
		instance = new BrotliAdapter();
	}

	@Test
	public void testArraySingleThread() throws IOException {
		final int i = new Random().nextInt(4);

		final byte[] original = testStrings[i].getBytes(CHARSET_NAME);

		final byte[] compressed = instance.compress(original, 2);
		final byte[] decompressed = instance.decompress(compressed);

		Assert.assertArrayEquals(original, decompressed);
		Assert.assertEquals(testStrings[i], new String(decompressed, CHARSET_NAME));
	}

	@Test
	public void testArrayMultiThread() throws IOException, InterruptedException {
		final Random random = new Random();

		for (int i = 0; i < JOBS_COUNT; i++) {
			final List<CompressDecompressThread> threads = new ArrayList<CompressDecompressThread>();
			for (int j = 0; j < CONCURRENT_THREADS; j++) {
				final CompressDecompressThread thread = new CompressDecompressThread(testStrings[random.nextInt(4)]);
				threads.add(thread);
				thread.start();
			}
			for (final Thread thread : threads) {
				thread.join();
			}

			for (final CompressDecompressThread thread : threads) {
				Assert.assertArrayEquals(thread.original.getBytes(CHARSET_NAME), thread.decompressed);
				Assert.assertEquals(thread.original, new String(thread.decompressed, CHARSET_NAME));
			}
			System.out.println('.');
		}
	}

	private class CompressDecompressThread extends Thread {

		private final String original;
		private byte[] decompressed;

		public CompressDecompressThread(final String original) {
			this.original = original;
		}

		@Override
		public void run() {
			try {
				final long t0 = System.nanoTime();
				final byte[] compressed = instance.compress(original.getBytes(CHARSET_NAME), 2);
				final long t1 = System.nanoTime();
				logger.log(Level.INFO, "Brotli compressed in {0,number,#} ms.", TimeUnit.NANOSECONDS.toMillis(t1 - t0));
				final long t2 = System.nanoTime();
				decompressed = instance.decompress(compressed);
				final long t3 = System.nanoTime();
				logger.log(Level.INFO, "Brotli decompressed in {0} ms.", Double.toString(TimeUnit.NANOSECONDS.toMicros(t3 - t2) / 1000d));
			}
			catch (final Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}
