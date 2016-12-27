package it.albertus.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ZipUtilsTest {

	private static File destination;
	private static File source;

	@BeforeClass
	public static void init() throws IOException {
		source = File.createTempFile(ZipUtilsTest.class.getSimpleName(), ".txt");
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(source);
			bw = new BufferedWriter(fw);
			bw.write("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
			bw.newLine();
		}
		finally {
			IOUtils.closeQuietly(bw);
			IOUtils.closeQuietly(fw);
		}
	}

	@AfterClass
	public static void destroy() {
		if (destination != null) {
			destination.delete();
		}
		if (source != null) {
			source.delete();
		}
	}

	@Test
	public void zip() throws IOException {
		destination = File.createTempFile(ZipUtilsTest.class.getSimpleName(), ZipUtils.ZIP_FILE_EXTENSION);
		ZipUtils.zip(destination, source);
	}

	@Test
	public void test() {
		ZipUtils.test(destination);
	}

}
