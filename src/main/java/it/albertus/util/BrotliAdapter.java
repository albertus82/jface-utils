package it.albertus.util;

import _Array.ArrayIterator;
import haxe.lang.EmptyObject;
import haxe.root.Array;
import haxe.root.Brotli;

/**
 * Adapter class for <b>BrotliHaxe</b> library.
 * 
 * @see <a href="https://github.com/dominikhlbg/BrotliHaxe">BrotliHaxe</a>
 */
public class BrotliAdapter {

	/** Minimum Brotli compression quality. */
	public static final int MIN_QUALITY = 0;

	/** Maximum Brotli compression quality. */
	public static final int MAX_QUALITY = 11;

	private final Brotli brotli;

	/** Construct a {@code BrotliAdapter} using the default dictionary. */
	public BrotliAdapter() {
		brotli = new Brotli(EmptyObject.EMPTY);
	}

	/**
	 * Construct a {@code BrotliAdapter} using a custom dictionary.
	 * 
	 * @param dictionaryPath the full path of the dictionary file.
	 */
	public BrotliAdapter(final String dictionaryPath) {
		brotli = new Brotli(dictionaryPath);
	}

	/**
	 * Compress a byte array using <b>Brotli</b> compression format.
	 * 
	 * @param uncompressed the byte array to compress
	 * @param quality the compression quality, valid range is
	 *        {@link #MIN_QUALITY} to {@link #MAX_QUALITY}
	 * @return the compressed byte array.
	 */
	@SuppressWarnings("unchecked")
	public byte[] compress(final byte[] uncompressed, final int quality) {
		if (quality < MIN_QUALITY || quality > MAX_QUALITY) {
			throw new IllegalArgumentException(quality + " - quality range is " + MIN_QUALITY + " to " + MAX_QUALITY);
		}
		final Array<Number> array = (Array<Number>) brotli.compressArray(Array.ofNative(toIntegerObject(uncompressed)), quality);

		final int length = array.length;
		final byte[] compressed = new byte[length];
		int i = 0;
		final ArrayIterator<Number> iterator = (ArrayIterator<Number>) array.iterator();
		while (i < length) {
			compressed[i++] = iterator.next().byteValue();
		}

		return compressed;
	}

	/**
	 * Decompress a byte array compressed using <b>Brotli</b> compression
	 * format.
	 * 
	 * @param compressed the compressed byte array
	 * @return the decompressed byte array
	 */
	@SuppressWarnings("unchecked")
	public byte[] decompress(final byte[] compressed) {
		final Array<Number> array = (Array<Number>) brotli.decompressArray(Array.ofNative(toIntegerObject(compressed)));

		final int length = array.length;
		final byte[] decompressed = new byte[length];
		int i = 0;
		final ArrayIterator<Number> iterator = (ArrayIterator<Number>) array.iterator();
		while (i < length) {
			decompressed[i++] = iterator.next().byteValue();
		}

		return decompressed;
	}

	static Integer[] toIntegerObject(final byte[] primitive) {
		final Integer[] wrapper = new Integer[primitive.length];
		for (int i = 0; i < primitive.length; i++) {
			wrapper[i] = primitive[i] & 0xFF;
		}
		return wrapper;
	}

}
