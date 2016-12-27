package it.albertus.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

	public static final String ZIP_FILE_EXTENSION = ".zip";

	private ZipUtils() {
		throw new IllegalAccessError("Utility class");
	}

	public static void zip(final File destination, final File... sources) throws IOException {
		if (!destination.exists() || !isValid(destination)) {
			FileOutputStream fos = null;
			ZipOutputStream zos = null;
			try {
				fos = new FileOutputStream(destination);
				zos = new ZipOutputStream(fos);
				zos.setLevel(Deflater.BEST_COMPRESSION);
				for (final File file : sources) {
					compress(zos, file);
				}
			}
			finally {
				IOUtils.closeQuietly(zos);
				IOUtils.closeQuietly(fos);
			}
		}
	}

	private static void compress(final ZipOutputStream zos, final File file) throws IOException {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			zos.putNextEntry(new ZipEntry(file.getName()));
			IOUtils.copy(bis, zos, 1024);
		}
		finally {
			zos.closeEntry();
			IOUtils.closeQuietly(bis);
			IOUtils.closeQuietly(fis);
		}
	}

	public static boolean isValid(final File zipFile) {
		try {
			test(zipFile);
			return true;
		}
		catch (final Exception e) {
			return false;
		}
	}

	public static void test(final File zipFile) throws IOException {
		ZipFile zf = null;
		try {
			zf = new ZipFile(zipFile);
			final Enumeration<? extends ZipEntry> e = zf.entries();
			if (!e.hasMoreElements()) {
				throw new IOException("No zip entries found");
			}
			while (e.hasMoreElements()) {
				final ZipEntry ze = e.nextElement();
				final long expectedCrc = ze.getCrc();
				final String fileName = ze.getName();
				InputStream is = null;
				CRC32OutputStream cos = null;
				try {
					is = zf.getInputStream(ze);
					cos = new CRC32OutputStream();
					IOUtils.copy(is, cos, 1024);
				}
				finally {
					IOUtils.closeQuietly(cos);
					IOUtils.closeQuietly(is);
				}
				final long actualCrc = cos.getValue();
				if (expectedCrc != actualCrc) {
					throw new IOException(String.format("Invalid CRC value for file \"%s\", expected 0x%08X, actual 0x%08X.", fileName, expectedCrc, actualCrc));
				}
			}
		}
		finally {
			IOUtils.closeQuietly(zf);
		}
	}

}
