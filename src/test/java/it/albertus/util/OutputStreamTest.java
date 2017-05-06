package it.albertus.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class OutputStreamTest {

	private static final int MIN_BUFFER_SIZE = 16;

	private static final String CHARSET = "UTF-8";

	private static final String originalString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
	private static File originalFile;

	private static final String expectedCrc16 = "a8e2";
	private static final String expectedCrc32 = "98b2c5bd";
	private static final String expectedMd5Hex = "db89bb5ceab87f9c0fcc2ab36c189c2c";
	private static final byte[] expectedMd5Bytes = { (byte) 0xDB, (byte) 0x89, (byte) 0xBB, 0x5C, (byte) 0xEA, (byte) 0xB8, 0x7F, (byte) 0x9C, 0x0F, (byte) 0xCC, 0x2A, (byte) 0xB3, 0x6C, 0x18, (byte) 0x9C, 0x2C };

	@BeforeClass
	public static void init() throws IOException {
		FileWriter fw = null;
		try {
			originalFile = File.createTempFile("original-", ".txt");
			fw = new FileWriter(originalFile);
			fw.write(originalString);
			System.out.println("Created original file \"" + originalFile + '"');
		}
		finally {
			IOUtils.closeQuietly(fw);
		}
	}

	@Test
	public void testCRC16OutputStream() throws IOException {
		CRC16OutputStream cos = new CRC16OutputStream();
		Assert.assertEquals("0000", cos.toString());

		final ByteArrayInputStream bais = new ByteArrayInputStream(originalString.getBytes(CHARSET));
		try {
			IOUtils.copy(bais, cos, Math.max(originalString.length() / 5, MIN_BUFFER_SIZE));
		}
		finally {
			IOUtils.closeQuietly(cos, bais);
		}
		Assert.assertEquals(expectedCrc16, cos.toString());
		Assert.assertEquals(expectedCrc16, cos.toString());

		cos.reset();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(originalFile);
			IOUtils.copy(fis, cos, (int) Math.max(originalFile.length(), MIN_BUFFER_SIZE));
		}
		finally {
			IOUtils.closeQuietly(cos, fis);
		}
		Assert.assertEquals(expectedCrc16, cos.toString());
		Assert.assertEquals(expectedCrc16, cos.toString());

		cos.reset();
		Assert.assertEquals("0000", cos.toString());
	}

	@Test
	public void testCRC32OutputStream() throws IOException {
		CRC32OutputStream cos = new CRC32OutputStream();
		Assert.assertEquals("00000000", cos.toString());

		final ByteArrayInputStream bais = new ByteArrayInputStream(originalString.getBytes(CHARSET));
		try {
			IOUtils.copy(bais, cos, Math.max(originalString.length() / 5, MIN_BUFFER_SIZE));
		}
		finally {
			IOUtils.closeQuietly(cos, bais);
		}
		Assert.assertEquals(expectedCrc32, cos.toString());
		Assert.assertEquals(expectedCrc32, cos.toString());

		cos.reset();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(originalFile);
			IOUtils.copy(fis, cos, (int) Math.max(originalFile.length(), MIN_BUFFER_SIZE));
		}
		finally {
			IOUtils.closeQuietly(cos, fis);
		}
		Assert.assertEquals(expectedCrc32, cos.toString());
		Assert.assertEquals(expectedCrc32, cos.toString());

		cos.reset();
		Assert.assertEquals("00000000", cos.toString());
	}

	@Test
	public void testDigestOutputStream() throws IOException, NoSuchAlgorithmException {
		DigestOutputStream dos = null;

		final ByteArrayInputStream bais = new ByteArrayInputStream(originalString.getBytes(CHARSET));
		try {
			dos = new DigestOutputStream("MD5");
			IOUtils.copy(bais, dos, Math.max(originalString.length() / 5, MIN_BUFFER_SIZE));
			Assert.assertEquals("", dos.toString());
			Assert.assertNull(dos.getValue());
		}
		finally {
			IOUtils.closeQuietly(dos, bais);
		}
		Assert.assertEquals(expectedMd5Hex, dos.toString());
		Assert.assertArrayEquals(expectedMd5Bytes, dos.getValue());
		Assert.assertEquals(expectedMd5Hex, dos.toString());
		Assert.assertArrayEquals(expectedMd5Bytes, dos.getValue());

		FileInputStream fis = null;
		try {
			dos = new DigestOutputStream("MD5");
			fis = new FileInputStream(originalFile);
			IOUtils.copy(fis, dos, (int) Math.max(originalFile.length() / 5, MIN_BUFFER_SIZE));
			Assert.assertNull(dos.getValue());
			Assert.assertEquals("", dos.toString());
		}
		finally {
			IOUtils.closeQuietly(dos, fis);
		}
		Assert.assertEquals(expectedMd5Hex, dos.toString());
		Assert.assertArrayEquals(expectedMd5Bytes, dos.getValue());
		Assert.assertEquals(expectedMd5Hex, dos.toString());
		Assert.assertArrayEquals(expectedMd5Bytes, dos.getValue());
	}

	@AfterClass
	public static void destroy() {
		if (originalFile.delete()) {
			System.out.println("Deleted original file \"" + originalFile + '"');
		}
		else {
			System.err.println("Cannot delete original file \"" + originalFile + '"');
			originalFile.deleteOnExit();
		}
	}

}
