package io.github.albertus82.util;

import java.io.UnsupportedEncodingException;
import java.util.zip.Checksum;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CRC16Test {

	private static byte[] bytes;

	@BeforeClass
	public static void init() throws UnsupportedEncodingException {
		bytes = "qwertyuiop".getBytes("UTF-8");
	}

	@Test
	public void testOk() throws UnsupportedEncodingException {
		final CRC16 crc = new CRC16();
		Assert.assertTrue(crc instanceof Checksum);
		Assert.assertEquals("0000", crc.toString());

		crc.update(bytes);
		Assert.assertEquals("76f5", crc.toString());

		crc.reset();

		crc.update(bytes, 2, 8);
		Assert.assertEquals("6ae5", crc.toString());

		crc.reset();

		crc.update(bytes, 2, 6); // ertyui
		Assert.assertEquals("691d", crc.toString()); // ertyui
		crc.update(bytes, 8, 2); // op
		Assert.assertEquals("6ae5", crc.toString()); // ertyuiop

		crc.reset();
		Assert.assertEquals("0000", crc.toString());
	}

	@Test
	public void testExceptions() {
		final CRC16 crc = new CRC16();

		try {
			crc.update(bytes, -1, -1);
			Assert.assertTrue(false);
		}
		catch (final ArrayIndexOutOfBoundsException e) {
			Assert.assertNotNull(e);
			e.printStackTrace();
		}

		try {
			crc.update(bytes, -5, 3);
			Assert.assertTrue(false);
		}
		catch (final ArrayIndexOutOfBoundsException e) {
			Assert.assertNotNull(e);
			e.printStackTrace();
		}

		try {
			crc.update(bytes, 0, -2);
			Assert.assertTrue(false);
		}
		catch (final ArrayIndexOutOfBoundsException e) {
			Assert.assertNotNull(e);
			e.printStackTrace();
		}

		try {
			crc.update(bytes, 8, 3);
			Assert.assertTrue(false);
		}
		catch (final ArrayIndexOutOfBoundsException e) {
			Assert.assertNotNull(e);
			e.printStackTrace();
		}

		try {
			crc.update(bytes, 10, 1);
			Assert.assertTrue(false);
		}
		catch (final ArrayIndexOutOfBoundsException e) {
			Assert.assertNotNull(e);
			e.printStackTrace();
		}

		try {
			crc.update(null, 1, 5);
			Assert.assertTrue(false);
		}
		catch (final NullPointerException e) {
			Assert.assertNotNull(e);
			e.printStackTrace();
		}

		try {
			crc.update((byte[]) null);
			Assert.assertTrue(false);
		}
		catch (final NullPointerException e) {
			Assert.assertNotNull(e);
			e.printStackTrace();
		}

		try {
			crc.update(null, 0, -2);
			Assert.assertTrue(false);
		}
		catch (final NullPointerException e) {
			Assert.assertNotNull(e);
			e.printStackTrace();
		}
	}

}
