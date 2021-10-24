package it.albertus.util.logging;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import it.albertus.util.ISupplier;

public class TimeBasedRollingFileHandlerTest {

	private static final Logger log = LoggerFactory.getLogger(TimeBasedRollingFileHandlerTest.class);

	@Test
	public void test() throws IOException {
		final FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String name) {
				return name.endsWith(".log");
			}
		};

		final ISupplier<Date> dateSupplier = new ISupplier<Date>() {
			private int count = 0;

			@Override
			public Date get() {
				switch (count++) {
				case 0:
					return new GregorianCalendar(2020, Calendar.JUNE, 30, 0, 0, 0).getTime();
				case 1:
					return new GregorianCalendar(2020, Calendar.JUNE, 30, 23, 30, 1).getTime();
				case 2:
					return new GregorianCalendar(2020, Calendar.JUNE, 30, 23, 45, 2).getTime();
				case 3:
					return new GregorianCalendar(2020, Calendar.JULY, 1, 0, 30, 3).getTime();
				case 4:
					return new GregorianCalendar(2020, Calendar.SEPTEMBER, 15, 15, 15, 4).getTime();
				case 5:
					return new GregorianCalendar(2020, Calendar.SEPTEMBER, 15, 15, 15, 5).getTime();
				default:
					return new GregorianCalendar(2021, Calendar.AUGUST, 16, 23, 59, 59).getTime();
				}
			}
		};

		final String uuid = UUID.randomUUID().toString().replace("-", "");
		final File tempFile = File.createTempFile(uuid, null);
		final File tempDir = new File(tempFile.getParent() + File.separator + uuid);
		if (!tempFile.delete()) {
			log.log(Level.WARNING, "Unable to delete temporary file \"{0}\".", tempFile);
			tempFile.deleteOnExit();
		}
		tempDir.mkdir();
		log.log(Level.INFO, "Created temporary directory \"{0}\".", tempDir);

		final Level originalRootLevel = LoggingSupport.getRootLogger().getLevel();
		final TimeBasedRollingFileHandlerConfig config = new TimeBasedRollingFileHandlerConfig();
		config.setLevel(Level.FINE);
		config.setFileNamePattern(tempDir + File.separator + "testLogFile_%d.log");
		TimeBasedRollingFileHandler handler = null;
		try {
			LoggingSupport.setRootLevel(Level.FINER);
			Assert.assertEquals(0, tempDir.listFiles().length);
			handler = new TimeBasedRollingFileHandler(config, dateSupplier);
			Assert.assertEquals(1, tempDir.listFiles(filter).length);
			Assert.assertEquals("testLogFile_20200630.log", tempDir.listFiles(filter)[0].getName());
			LoggingSupport.getRootLogger().addHandler(handler);
			log.info("1");
			Assert.assertEquals(1, tempDir.listFiles(filter).length);
			Assert.assertEquals("testLogFile_20200630.log", tempDir.listFiles(filter)[0].getName());
			log.info("2");
			Assert.assertEquals(1, tempDir.listFiles(filter).length);
			Assert.assertEquals("testLogFile_20200630.log", tempDir.listFiles(filter)[0].getName());
			log.fine("3");
			Assert.assertEquals(2, tempDir.listFiles(filter).length);
			Assert.assertArrayEquals(new String[] { "testLogFile_20200630.log", "testLogFile_20200701.log" }, toFileNameArray(tempDir.listFiles(filter)));
			log.finest("4");
			Assert.assertEquals(2, tempDir.listFiles(filter).length);
			Assert.assertArrayEquals(new String[] { "testLogFile_20200630.log", "testLogFile_20200701.log" }, toFileNameArray(tempDir.listFiles(filter)));
			log.info("4");
			Assert.assertEquals(3, tempDir.listFiles(filter).length);
			Assert.assertArrayEquals(new String[] { "testLogFile_20200630.log", "testLogFile_20200701.log", "testLogFile_20200915.log" }, toFileNameArray(tempDir.listFiles(filter)));
			log.fine("5");
			Assert.assertEquals(3, tempDir.listFiles(filter).length);
			Assert.assertArrayEquals(new String[] { "testLogFile_20200630.log", "testLogFile_20200701.log", "testLogFile_20200915.log" }, toFileNameArray(tempDir.listFiles(filter)));
			log.info("6");
			Assert.assertEquals(4, tempDir.listFiles(filter).length);
			Assert.assertArrayEquals(new String[] { "testLogFile_20200630.log", "testLogFile_20200701.log", "testLogFile_20200915.log", "testLogFile_20210816.log" }, toFileNameArray(tempDir.listFiles(filter)));
			log.finer("7");
			Assert.assertEquals(4, tempDir.listFiles(filter).length);
			Assert.assertArrayEquals(new String[] { "testLogFile_20200630.log", "testLogFile_20200701.log", "testLogFile_20200915.log", "testLogFile_20210816.log" }, toFileNameArray(tempDir.listFiles(filter)));
			log.info("8");
			Assert.assertEquals(4, tempDir.listFiles(filter).length);
			Assert.assertArrayEquals(new String[] { "testLogFile_20200630.log", "testLogFile_20200701.log", "testLogFile_20200915.log", "testLogFile_20210816.log" }, toFileNameArray(tempDir.listFiles(filter)));
		}
		finally {
			LoggingSupport.setRootLevel(originalRootLevel);
			if (handler != null) {
				LoggingSupport.getRootLogger().removeHandler(handler);
				handler.close();
			}
			try {
				FileUtils.deleteDirectory(tempDir);
				log.log(Level.INFO, "Deleted temporary directory \"{0}\".", tempDir);
			}
			catch (final IOException e) {
				log.log(Level.WARNING, "Cannot delete temporary directory \"" + tempDir + "\":", e);
			}
		}
	}

	private static String[] toFileNameArray(final File[] files) {
		final List<String> fileNames = new ArrayList<String>();
		for (final File file : files) {
			fileNames.add(file.getName());
		}
		Collections.sort(fileNames);
		return fileNames.toArray(new String[0]);
	}

}
