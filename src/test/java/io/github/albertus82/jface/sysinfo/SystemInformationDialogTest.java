package io.github.albertus82.jface.sysinfo;

import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import io.github.albertus82.util.logging.LoggerFactory;

public class SystemInformationDialogTest {

	private static final Logger log = LoggerFactory.getLogger(SystemInformationDialogTest.class);

	@Test
	public void testIsAvailable() {
		Assert.assertTrue(SystemInformationDialog.isAvailable());
	}

}
