package it.albertus.util;

import _Array.ArrayIterator;
import haxe.lang.EmptyObject;
import haxe.root.Array;
import haxe.root.Brotli;

public class BrotliAdapter {

	private final Brotli instance = new Brotli(EmptyObject.EMPTY);

	@SuppressWarnings("unchecked")
	public byte[] compress(final byte[] uncompressed) {
		final Array<Number> array = (Array<Number>) instance.compressArray(Array.ofNative(toIntegerObject(uncompressed)), 1);

		final byte[] compressed = new byte[array.length];
		int i = 0;
		final ArrayIterator<Number> iterator = (ArrayIterator<Number>) array.iterator();
		while (iterator.hasNext()) {
			compressed[i++] = iterator.next().byteValue();
		}

		return compressed;
	}

	@SuppressWarnings("unchecked")
	public byte[] decompress(final byte[] compressed) {
		final Array<Number> array = (Array<Number>) instance.decompressArray(Array.ofNative(toIntegerObject(compressed)));

		final byte[] decompressed = new byte[array.length];
		int i = 0;
		final ArrayIterator<Number> iterator = (ArrayIterator<Number>) array.iterator();
		while (iterator.hasNext()) {
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
