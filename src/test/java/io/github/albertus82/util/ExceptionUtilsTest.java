package io.github.albertus82.util;

import org.junit.Assert;
import org.junit.Test;

public class ExceptionUtilsTest {

	@Test
	public void getStackTrace() {
		final String stackTrace = ExceptionUtils.getStackTrace(new RuntimeException("Exception message!"));
		System.out.println(stackTrace);
		Assert.assertNotNull(stackTrace);
		Assert.assertNotEquals("", stackTrace);
	}

}
