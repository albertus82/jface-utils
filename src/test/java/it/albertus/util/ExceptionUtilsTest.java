package it.albertus.util;

import org.junit.Assert;
import org.junit.Test;

public class ExceptionUtilsTest {

	@Test
	public void getStackTrace() {
		Assert.assertNotEquals("", ExceptionUtils.getStackTrace(new RuntimeException()));
	}

}
