package it.albertus.httpserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import it.albertus.util.IOUtils;

public class AbstractHttpHandlerTest {

	private static final String CHARSET = "UTF-8";

	private static final String originalString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
	private static File originalFile;

	private static final String expectedEtag = "\"db89bb5ceab87f9c0fcc2ab36c189c2c\"";
	private static final String expectedContentMd5 = "24m7XOq4f5wPzCqzbBicLA=="; // DatatypeConverter.printBase64Binary(new byte[] { (byte) 0xDB, (byte) 0x89, (byte) 0xBB, 0x5C, (byte) 0xEA, (byte) 0xB8, 0x7F, (byte) 0x9C, 0x0F, (byte) 0xCC, 0x2A, (byte) 0xB3, 0x6C, 0x18, (byte) 0x9C, 0x2C }); // "db89bb5ceab87f9c0fcc2ab36c189c2c";

	private static AbstractHttpHandler handler;

	@BeforeClass
	public static void init() throws IOException {
		handler = new AbstractHttpHandler(null) {};
		originalFile = File.createTempFile(AbstractHttpHandlerTest.class.getSimpleName() + '-', null);
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(originalFile);
			bw = new BufferedWriter(fw);
			bw.write(originalString);
			System.out.println("Created temporary file \"" + originalFile + '"');
		}
		finally {
			IOUtils.closeQuietly(bw, fw);
		}
		Assert.assertEquals(originalString.length(), originalFile.length());
	}

	@Test
	public void generateEtagTest() throws IOException {
		Assert.assertEquals(expectedEtag, handler.generateEtag(originalString.getBytes(CHARSET)));
		Assert.assertEquals(expectedEtag, handler.generateEtag(originalFile));
	}

	@Test
	public void generateContentMd5Test() throws IOException, NoSuchAlgorithmException {
		Assert.assertEquals(expectedContentMd5, handler.generateContentMd5(originalString.getBytes(CHARSET)));
		Assert.assertEquals(expectedContentMd5, handler.generateContentMd5(originalFile));
	}

	@Test
	public void getContentTypeTest() {
		Assert.assertEquals("image/x-icon", handler.getContentType("asdfghjkl.ICO "));
		Assert.assertEquals("text/css", handler.getContentType("zxcvbnm.css"));
		Assert.assertEquals("application/javascript", handler.getContentType("qwertyuiop.Js   "));
		Assert.assertEquals("application/json", handler.getContentType(" qwert-yuiop.json"));
		Assert.assertEquals("application/xml", handler.getContentType("asdfghjkl.XML"));
		Assert.assertEquals("application/xhtml+xml", handler.getContentType("zxcvbnm.xHtml"));
		Assert.assertEquals("application/pdf", handler.getContentType("qwertyuiop.pdf"));
		Assert.assertEquals("text/html", handler.getContentType("qwertyuiop.HTML"));
		Assert.assertEquals("text/html", handler.getContentType("asdfg hjkl.htm"));
		Assert.assertEquals("application/octet-stream", handler.getContentType("zxcvbnm"));
		Assert.assertEquals("image/jpeg", handler.getContentType("zxcvbnm.jpeg"));
		Assert.assertEquals("image/jpeg", handler.getContentType("zxcvbnm.jpg"));
		Assert.assertEquals("image/gif", handler.getContentType("qwertyuiop.gif"));
		Assert.assertEquals("image/png", handler.getContentType("asdfghjkl.png"));
		Assert.assertEquals("application/octet-stream", handler.getContentType("asdfghjkl."));
		Assert.assertEquals("text/x-log", handler.getContentType(".log"));
	}

	@AfterClass
	public static void destroy() {
		if (originalFile.delete()) {
			System.out.println("Deleted temporary file \"" + originalFile + '"');
		}
		else {
			System.err.println("Cannot delete temporary file \"" + originalFile + '"');
			originalFile.deleteOnExit();
		}
	}

}
