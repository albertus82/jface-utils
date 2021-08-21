package it.albertus.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import it.albertus.util.logging.LoggerFactory;

public class ZipUtils {

	public static final String ZIP_FILE_EXTENSION = ".zip";

	private static final Logger log = LoggerFactory.getLogger(ZipUtils.class);

	private static final int THRESHOLD_ENTRIES = 0xFFFF;

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
				IOUtils.closeQuietly(zos, fos);
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
			try {
				zos.closeEntry();
			}
			finally {
				IOUtils.closeQuietly(bis, fis);
			}
		}
	}

	public static boolean isValid(final File zipFile) {
		try {
			test(zipFile);
			return true;
		}
		catch (final Exception e) {
			log.log(Level.FINE, "An error occurred while validating the ZIP file:", e);
			return false;
		}
	}

	public static void test(final File zipFile) throws IOException {
		final String currentPath = new File("").getCanonicalPath();
		log.log(Level.FINER, "currentPath: {0}", currentPath);
		ZipFile zf = null;
		try {
			zf = new ZipFile(zipFile);
			final Enumeration<? extends ZipEntry> e = zf.entries();
			if (!e.hasMoreElements()) {
				throw new IOException("No ZIP entries found");
			}
			int entryCount = 0;
			while (e.hasMoreElements()) {
				if (++entryCount > THRESHOLD_ENTRIES) {
					throw new SecurityException("Too many ZIP entries");
				}
				final ZipEntry ze = e.nextElement();
				final long expectedCrc = ze.getCrc();
				final String fileName = ze.getName();
				final String entryPath = new File(currentPath, fileName).getCanonicalPath();
				log.log(Level.FINER, "entryPath: {0}", entryPath);
				if (!entryPath.startsWith(currentPath)) { // https://blog.ripstech.com/2019/hidden-flaws-of-archives-java/
					throw new SecurityException("ZIP entry not within target directory");
				}
				InputStream is = null;
				CRC32OutputStream cos = null;
				try {
					is = zf.getInputStream(ze);
					cos = new CRC32OutputStream();
					IOUtils.copy(is, cos, 1024);
				}
				finally {
					IOUtils.closeQuietly(cos, is);
				}
				final long actualCrc = cos.getValue();
				if (expectedCrc != actualCrc) {
					throw new IOException(String.format("Invalid CRC value for file \"%s\", expected 0x%08X, actual 0x%08X", fileName, expectedCrc, actualCrc));
				}
			}
		}
		finally {
			IOUtils.closeQuietly(zf);
		}
	}

}
