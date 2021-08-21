package it.albertus.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import it.albertus.util.logging.LoggerFactory;

public class LoggerFactoryTest {

	private static Logger log;

	@BeforeClass
	public static void init() {
		log = LoggerFactory.getLogger(LoggerFactoryTest.class);
	}

	@Test
	public void message() {
		log.log(Level.INFO, "Log message.");
		Assert.assertTrue(true);
	}

	@Test
	public void exception() {
		final RuntimeException exception = new RuntimeException("Exception message!");
		log.log(Level.WARNING, exception.getMessage(), exception);
		Assert.assertTrue(true);
	}

}
