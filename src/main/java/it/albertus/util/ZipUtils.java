package it.albertus.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

	public static final String ZIP_FILE_EXTENSION = ".zip";

	public static void zip(final File destination, final File... sources) throws IOException {
		if (!destination.exists() || !test(destination)) {
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
			IOUtils.copy(bis, zos, new byte[1024]);
		}
		finally {
			zos.closeEntry();
			IOUtils.closeQuietly(bis);
			IOUtils.closeQuietly(fis);
		}
	}

	public static boolean test(final File zipFile) {
		ZipFile zf = null;
		FileInputStream fis = null;
		ZipInputStream zis = null;
		try {
			zf = new ZipFile(zipFile);
			fis = new FileInputStream(zipFile);
			zis = new ZipInputStream(fis);
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
			IOUtils.closeQuietly(zf);
			IOUtils.closeQuietly(zis);
			IOUtils.closeQuietly(fis);
		}
		return true;
	}

	protected ZipUtils() {
		// throw new IllegalAccessError("Utility class"); // FIXME uncomment and make private
	}

}
