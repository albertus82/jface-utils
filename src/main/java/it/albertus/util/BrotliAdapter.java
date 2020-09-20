package it.albertus.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.brotli.dec.BrotliInputStream;

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

	/** Minimum Brotli compression quality ({@code 0}). */
	public static final int MIN_QUALITY = 0;

	/** Maximum Brotli compression quality ({@code 11}). */
	public static final int MAX_QUALITY = 11;

	private final Brotli brotli;
	private final byte[] customDictionary;

	/** Construct a {@code BrotliAdapter} using the default dictionary. */
	public BrotliAdapter() {
		brotli = new Brotli(EmptyObject.EMPTY);
		customDictionary = null;
	}

	/**
	 * Construct a {@code BrotliAdapter} using a custom dictionary.
	 * 
	 * @param dictionaryPath the full path of the dictionary file.
	 * 
	 * @throws IOException if a read error occurs.
	 */
	public BrotliAdapter(final String dictionaryPath) throws IOException {
		brotli = new Brotli(dictionaryPath);
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		try {
			fis = new FileInputStream(dictionaryPath);
			baos = new ByteArrayOutputStream();
			IOUtils.copy(fis, baos, 8192);
		}
		finally {
			IOUtils.closeQuietly(baos, fis);
		}
		customDictionary = baos.toByteArray();
	}

	/**
	 * Compress a byte array using <b>Brotli</b> compression format.
	 * 
	 * @param uncompressed the byte array to compress
	 * @param quality      the compression quality, valid range is
	 *                     {@value #MIN_QUALITY} to {@value #MAX_QUALITY}
	 * 
	 * @return the compressed byte array.
	 */
	@SuppressWarnings("unchecked")
	public byte[] compress(final byte[] uncompressed, final int quality) {
		if (quality < MIN_QUALITY || quality > MAX_QUALITY) {
			throw new IllegalArgumentException(quality + " - quality range is " + MIN_QUALITY + " to " + MAX_QUALITY);
		}
		return toNative((Array<Number>) brotli.compressArray(Array.ofNative(toIntegerObject(uncompressed)), quality));
	}

	/**
	 * Decompress a byte array compressed using <b>Brotli</b> compression format.
	 * 
	 * @param compressed the compressed byte array
	 * 
	 * @return the decompressed byte array
	 * 
	 * @throws IOException in case of corrupted data or source stream problems
	 */
	public byte[] decompress(final byte[] compressed) throws IOException {
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		BrotliInputStream bis = null;
		try {
			bais = new ByteArrayInputStream(compressed);
			baos = new ByteArrayOutputStream();
			bis = new BrotliInputStream(bais, BrotliInputStream.DEFAULT_INTERNAL_BUFFER_SIZE, customDictionary);
			IOUtils.copy(bis, baos, 8192);
		}
		finally {
			IOUtils.closeQuietly(baos, bis, bais);
		}
		return baos.toByteArray();
	}

	@SuppressWarnings("unchecked")
	private static byte[] toNative(final Array<? extends Number> haxeArray) {
		final int length = haxeArray.length;
		final byte[] nativeArray = new byte[length];
		int i = 0;
		final ArrayIterator<? extends Number> iterator = (ArrayIterator<? extends Number>) haxeArray.iterator();
		while (i < length) {
			nativeArray[i++] = iterator.next().byteValue();
		}
		return nativeArray;
	}

	private static Integer[] toIntegerObject(final byte[] primitive) {
		final Integer[] wrapper = new Integer[primitive.length];
		for (int i = 0; i < primitive.length; i++) {
			wrapper[i] = primitive[i] & 0xFF;
		}
		return wrapper;
	}

}
