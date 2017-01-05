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

	private static final String CHARSET = "UTF-8";

	private static final String originalString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
	private static File originalFile;

	private static final String expectedCrc32 = "98b2c5bd";
	private static final String expectedMd5 = "db89bb5ceab87f9c0fcc2ab36c189c2c";

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
	public void testCRC32OutputStream() throws IOException {
		CRC32OutputStream cos = new CRC32OutputStream();

		final ByteArrayInputStream bais = new ByteArrayInputStream(originalString.getBytes(CHARSET));
		try {
			IOUtils.copy(bais, cos, Math.max(originalString.length() / 5, 16));
		}
		finally {
			IOUtils.closeQuietly(cos, bais);
		}
		Assert.assertEquals(expectedCrc32, cos.toString());

		cos.reset();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(originalFile);
			IOUtils.copy(fis, cos, (int) Math.max(originalFile.length() / 5, 16));
		}
		finally {
			IOUtils.closeQuietly(cos, fis);
		}
		Assert.assertEquals(expectedCrc32, cos.toString());
	}

	@Test
	public void testDigestOutputStream() throws IOException, NoSuchAlgorithmException {
		DigestOutputStream dos = null;

		final ByteArrayInputStream bais = new ByteArrayInputStream(originalString.getBytes(CHARSET));
		try {
			dos = new DigestOutputStream("MD5");
			IOUtils.copy(bais, dos, Math.max(originalString.length() / 5, 16));
		}
		finally {
			IOUtils.closeQuietly(dos, bais);
		}
		Assert.assertEquals(expectedMd5, dos.toString());

		dos.reset();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(originalFile);
			IOUtils.copy(fis, dos, (int) Math.max(originalFile.length() / 5, 16));
		}
		finally {
			IOUtils.closeQuietly(dos, fis);
		}
		Assert.assertEquals(expectedMd5, dos.toString());
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
