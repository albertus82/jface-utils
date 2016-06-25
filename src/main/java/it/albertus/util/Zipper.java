package it.albertus.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zipper {

	public static final String ZIP_FILE_EXTENSION = ".zip";

	private static class Singleton {
		private static final Zipper instance = new Zipper();
	}

	public static Zipper getInstance() {
		return Singleton.instance;
	}

	private Zipper() {}

	public void zip(final File destination, final File... sources) throws IOException {
		if (!destination.exists() || !test(destination)) {
			ZipOutputStream output = null;
			InputStream input = null;
			try {
				output = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));
				output.setLevel(Deflater.BEST_COMPRESSION);
				for (final File file : sources) {
					try {
						input = new BufferedInputStream(new FileInputStream(file));
						output.putNextEntry(new ZipEntry(file.getName()));
						final byte[] buffer = new byte[1024];
						int length;
						while ((length = input.read(buffer)) > 0) {
							output.write(buffer, 0, length);
						}
						output.closeEntry();
					}
					finally {
						try {
							input.close();
						}
						catch (final Exception e) {}
					}
				}
			}
			finally {
				try {
					output.close();
				}
				catch (final Exception e) {}
				try {
					input.close();
				}
				catch (final Exception e) {}
			}
		}
	}

	public boolean test(final File zipFile) {
		ZipFile zf = null;
		ZipInputStream zis = null;
		try {
			zf = new ZipFile(zipFile);
			zis = new ZipInputStream(new FileInputStream(zipFile));
			ZipEntry ze = zis.getNextEntry();
			if (ze == null) {
				return false;
			}
			while (ze != null) {
				zf.getInputStream(ze);
				ze.getCrc();
				ze.getCompressedSize();
				ze.getName();
				ze = zis.getNextEntry();
			}
		}
		catch (final Exception exception) {
			return false;
		}
		finally {
			try {
				zf.close();
			}
			catch (final Exception e) {}
			try {
				zis.close();
			}
			catch (final Exception e) {}
		}
		return true;
	}

}
