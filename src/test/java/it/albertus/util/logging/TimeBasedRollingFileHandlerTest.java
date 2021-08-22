package it.albertus.util.logging;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import it.albertus.util.ISupplier;

public class TimeBasedRollingFileHandlerTest {

	private static final Logger log = LoggerFactory.getLogger(TimeBasedRollingFileHandlerTest.class);

	private static int count = 0;

	@Test
	public void test() throws IOException {
		final FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".log");
			}
		};

		final ISupplier<Date> dateSupplier = new ISupplier<Date>() {
			@Override
			public Date get() {
				switch (count++) {
				case 0:
					return new GregorianCalendar(2020, Calendar.JUNE, 30, 23, 30, 1).getTime();
				case 1:
					return new GregorianCalendar(2020, Calendar.JUNE, 30, 23, 30, 2).getTime();
				case 2:
					return new GregorianCalendar(2020, Calendar.JUNE, 30, 23, 45, 3).getTime();
				case 3:
					return new GregorianCalendar(2020, Calendar.JULY, 1, 0, 30, 4).getTime();
				case 4:
					return new GregorianCalendar(2020, Calendar.SEPTEMBER, 15, 15, 15, 5).getTime();
				case 5:
					return new GregorianCalendar(2020, Calendar.SEPTEMBER, 15, 15, 15, 6).getTime();
				default:
					return new GregorianCalendar(2021, Calendar.AUGUST, 16, 16, 16, 59).getTime();
				}
			}
		};

		final String uuid = UUID.randomUUID().toString().replace("-", "");
		final File tempFile = File.createTempFile(uuid, null);
		final File tempDir = new File(tempFile.getParent() + File.separator + uuid);
		if (!tempFile.delete()) {
			log.log(Level.WARNING, "Unable to delete temporary file {0}.", tempFile);
			tempFile.deleteOnExit();
		}
		tempDir.mkdir();
		log.log(Level.INFO, "Created temporary directory {0}", tempDir);

		final TimeBasedRollingFileHandlerConfig config = new TimeBasedRollingFileHandlerConfig();
		config.setFileNamePattern(tempDir + File.separator + "logfile_%d.log");
		Assert.assertEquals(0, tempDir.listFiles().length);
		TimeBasedRollingFileHandler handler = null;
		try {
			handler = new TimeBasedRollingFileHandler(config, dateSupplier);
			Assert.assertEquals(1, tempDir.listFiles(filter).length);
			Assert.assertEquals("logfile_20200630.log", tempDir.listFiles(filter)[0].getName());
			LoggingSupport.getRootLogger().addHandler(handler);
			log.info("a");
			Assert.assertEquals(1, tempDir.listFiles(filter).length);
			Assert.assertEquals("logfile_20200630.log", tempDir.listFiles(filter)[0].getName());
			log.info("b");
			Assert.assertEquals(1, tempDir.listFiles(filter).length);
			Assert.assertEquals("logfile_20200630.log", tempDir.listFiles(filter)[0].getName());
			log.info("c");
			Assert.assertEquals(2, tempDir.listFiles(filter).length);
			log.info("d");
			Assert.assertEquals(3, tempDir.listFiles(filter).length);
			log.info("e");
			Assert.assertEquals(3, tempDir.listFiles(filter).length);
			log.info("f");
			Assert.assertEquals(4, tempDir.listFiles(filter).length);
			log.info("g");
			Assert.assertEquals(4, tempDir.listFiles(filter).length);
		}
		catch (final Exception e) {
			log.log(Level.SEVERE, e.toString(), e);
			assertFalse(true);
		}
		finally {
			LoggingSupport.getRootLogger().removeHandler(handler);
			handler.close();
			FileUtils.deleteDirectory(tempDir);
		}
	}

}
