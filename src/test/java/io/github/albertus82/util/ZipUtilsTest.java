package io.github.albertus82.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
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
			bw.write("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum ultricies sapien et mauris euismod, sed tincidunt quam volutpat. Aliquam vestibulum urna at magna rutrum convallis. Aenean eu ultricies nunc. Ut tempus, nunc a tincidunt sodales, orci diam eleifend nisi, nec commodo turpis nibh ac turpis. Duis dictum tempus mauris, at pellentesque ligula feugiat quis. In eleifend ornare leo vestibulum dapibus. Sed eu diam elit. Nam hendrerit convallis diam, ");
			bw.write("id porta leo eleifend quis. Praesent vitae lorem eget eros scelerisque convallis vel non purus. Maecenas congue massa ac est consequat faucibus. Integer convallis ante sed nibh rutrum, at elementum neque mattis. Vivamus porttitor erat non suscipit maximus. Cras egestas erat sit amet vestibulum pellentesque. Sed ante lacus, mollis et rhoncus a, tempus sed sem. Donec tempus maximus luctus. Vivamus consectetur augue et nisl venenatis, sed bibendum leo aliquet.");
			bw.newLine();
			bw.write("Morbi dictum accumsan ornare. Duis scelerisque cursus lorem, id pretium leo facilisis at. Donec malesuada vulputate sem molestie tincidunt. Praesent dapibus malesuada purus vitae congue. Mauris eget erat ultricies, scelerisque dui et, ultricies quam. Phasellus elementum nunc convallis eleifend aliquet. Vestibulum egestas purus id ex mattis, vel sodales libero ullamcorper. Nam mollis scelerisque ex ac ullamcorper. ");
			bw.write("Duis dignissim neque dolor, sit amet fringilla mi sollicitudin quis. Proin varius, nisi sit amet ultrices porttitor, ligula dui fermentum sem, a cursus risus erat tempor mi. Ut maximus felis et risus convallis bibendum. Proin sit amet orci a tellus scelerisque volutpat. Donec aliquam lacinia sem, a porta purus venenatis eget.");
			bw.newLine();
			bw.write("Mauris in nulla at felis lobortis semper vel finibus nisl. Aliquam erat volutpat. Aenean quis mattis elit. Nullam eleifend dictum auctor. Mauris arcu nisl, varius non varius at, pulvinar sit amet nisl. Nam urna orci, dignissim vel nunc eu, tempor imperdiet justo. Sed et faucibus lorem. ");
			bw.write("Vivamus turpis ligula, porta nec lorem sit amet, luctus varius ipsum. Sed semper libero sed orci ultricies malesuada. Cras quam odio, consectetur non ex quis, rhoncus bibendum libero. Cras mattis est nec nisl rutrum tincidunt.");
			bw.newLine();
			bw.write("Donec tincidunt efficitur eleifend. Nulla sollicitudin velit et risus cursus consequat. Sed commodo, turpis nec tempus sollicitudin, elit nisi hendrerit augue, vitae condimentum velit purus a ipsum. Proin id ligula a massa commodo finibus. Etiam a bibendum turpis. Duis pharetra sit amet nunc mollis dictum. Nullam mattis erat nec diam laoreet posuere. Etiam quis diam consequat, vulputate est nec, tristique ipsum.");
			bw.newLine();
			bw.write("Suspendisse iaculis eleifend sapien at accumsan. Suspendisse arcu ipsum, porttitor sit amet eros id, molestie lobortis diam. In hac habitasse platea dictumst. Donec ut libero nec turpis fringilla molestie vel nec enim. Phasellus ut faucibus erat. Nam sed turpis consequat, malesuada orci nec, posuere turpis. Nullam nulla risus, tincidunt eu eleifend ac, convallis id arcu. Quisque et viverra ipsum. Fusce pellentesque eros et mi fringilla vestibulum. ");
			bw.write("Etiam pharetra justo sed cursus convallis. Donec placerat diam tincidunt ligula hendrerit iaculis. Cras volutpat leo enim, sit amet tristique diam ultricies id. Suspendisse iaculis id augue tempus dignissim.");
			bw.newLine();
			bw.write("Fusce ac ante convallis, vehicula mi nec, ornare nulla. Aenean interdum molestie sapien, eu aliquam sem eleifend nec. In tempor nunc a fermentum pulvinar. Nunc feugiat dui lacus, vel euismod risus rhoncus eu. Nullam eget molestie lectus. Integer volutpat nec odio sit amet iaculis. Curabitur facilisis, nisl sit amet tristique finibus, libero mi rutrum justo, et hendrerit elit orci a tellus. Vestibulum finibus metus at vulputate imperdiet. Sed ultrices aliquam maximus.");
			bw.newLine();
			bw.write("Nunc cursus consequat augue, nec finibus arcu porttitor sed. In euismod elit sit amet ipsum suscipit gravida. Duis feugiat orci nec ligula interdum, egestas congue nunc venenatis. Proin interdum risus luctus diam rutrum iaculis. Quisque cursus tempus ipsum non sodales metus.");
			bw.newLine();
		}
		finally {
			IOUtils.closeQuietly(bw, fw);
		}
	}

	@AfterClass
	public static void destroy() {
		if (destination != null && !destination.delete()) {
			destination.deleteOnExit();
		}
		if (source != null && !source.delete()) {
			source.deleteOnExit();
		}
	}

	@Test
	public void zip() throws IOException {
		final File temp = File.createTempFile(ZipUtilsTest.class.getSimpleName(), ZipUtils.ZIP_FILE_EXTENSION);
		temp.delete();
		destination = new File(temp.getPath());
		ZipUtils.zip(destination, source);
		Assert.assertTrue(true);
	}

	@Test
	public void test() throws IOException {
		// Test good zip file
		ZipUtils.test(destination);

		// Test damaged zip file
		File temp = null;
		try {
			temp = getDamagedCopy();
			ZipUtils.test(temp);
			Assert.assertTrue(false);
		}
		catch (final IOException ioe) {
			ioe.printStackTrace();
			Assert.assertTrue(true);
		}
		finally {
			if (temp != null && !temp.delete()) {
				temp.deleteOnExit();
			}
		}
	}

	@Test
	public void isValid() throws IOException {
		// Test good zip file
		Assert.assertTrue(ZipUtils.isValid(destination));

		// Test damaged zip file
		File temp = null;
		try {
			temp = getDamagedCopy();
			Assert.assertFalse(ZipUtils.isValid(temp));
		}
		finally {
			if (temp != null && !temp.delete()) {
				temp.deleteOnExit();
			}
		}
	}

	private File getDamagedCopy() throws IOException {
		final File temp = File.createTempFile(ZipUtilsTest.class.getSimpleName(), ZipUtils.ZIP_FILE_EXTENSION);
		temp.delete();

		final FileInputStream fis = new FileInputStream(destination);
		final FileOutputStream fos = new FileOutputStream(temp);
		try {
			int errors = 0;
			int bt;
			while (IOUtils.EOF != (bt = fis.read())) {
				final int error = (int) (Math.random() + .01);
				fos.write(bt + error);
				errors += error != 0 ? 1 : 0;
			}
			System.out.println("Added " + errors + " errors to the new file.");
			if (errors < 1) {
				throw new IllegalStateException("No errors added");
			}
		}
		finally {
			IOUtils.closeQuietly(fos, fis);
		}

		return temp;
	}

}
