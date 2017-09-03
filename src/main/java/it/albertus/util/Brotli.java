package it.albertus.util;

import haxe.lang.EmptyObject;
import haxe.root.Array;

public class Brotli {

	private final haxe.root.Brotli instance = new haxe.root.Brotli(EmptyObject.EMPTY);

	public byte[] compress(final byte[] uncompressed) {
		final Array<?> array = (Array<?>) instance.compressArray(Array.ofNative(toIntegerObject(uncompressed)), 1);

		final int length = array.length;
		final byte[] compressed = new byte[length];
		for (int i = length - 1; i >= 0; i--) {
			compressed[i] = ((Number) array.pop()).byteValue();
		}

		return compressed;
	}

	public byte[] decompress(final byte[] compressed) {
		final Array<?> array = (Array<?>) instance.decompressArray(Array.ofNative(toIntegerObject(compressed)));

		final int length = array.length;
		final byte[] decompressed = new byte[length];
		for (int i = length - 1; i >= 0; i--) {
			decompressed[i] = ((Number) array.pop()).byteValue();
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
