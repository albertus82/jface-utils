package it.albertus.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class LoggerFactoryTest {

	private static Logger logger;

	@BeforeClass
	public static void init() {
		logger = LoggerFactory.getLogger(LoggerFactoryTest.class);
	}

	@Test
	public void message() {
		logger.log(Level.INFO, "Log message.");
		Assert.assertTrue(true);
	}

	@Test
	public void exception() {
		final RuntimeException exception = new RuntimeException("Exception message!");
		logger.log(Level.WARNING, exception.getMessage(), exception);
		Assert.assertTrue(true);
	}

}
