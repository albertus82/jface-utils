package io.github.albertus82.jface;

import org.junit.Assert;
import org.junit.Test;

public class SwtUtilsTest {

	@Test
	public void testIsGtk3() {
		Assert.assertFalse(SwtUtils.isGtk3(false, 4701, null, null));
		Assert.assertFalse(SwtUtils.isGtk3(false, 4712, null, "1"));
		Assert.assertFalse(SwtUtils.isGtk3(false, 4799, null, "0"));

		Assert.assertFalse(SwtUtils.isGtk3(true, 4101, null, null));
		Assert.assertFalse(SwtUtils.isGtk3(true, 4112, null, "0"));
		Assert.assertFalse(SwtUtils.isGtk3(true, 4199, null, "1"));

		Assert.assertFalse(SwtUtils.isGtk3(true, 4201, null, null));
		Assert.assertFalse(SwtUtils.isGtk3(true, 4212, null, "0"));
		Assert.assertFalse(SwtUtils.isGtk3(true, 4299, null, "1"));

		Assert.assertFalse(SwtUtils.isGtk3(true, 4301, null, null));
		Assert.assertFalse(SwtUtils.isGtk3(true, 4312, null, "0"));
		Assert.assertTrue(SwtUtils.isGtk3(true, 4399, null, "1"));

		Assert.assertTrue(SwtUtils.isGtk3(true, 4401, null, null));
		Assert.assertFalse(SwtUtils.isGtk3(true, 4412, null, "0"));
		Assert.assertTrue(SwtUtils.isGtk3(true, 4499, null, "1"));

		Assert.assertTrue(SwtUtils.isGtk3(true, 4501, null, null));
		Assert.assertFalse(SwtUtils.isGtk3(true, 4512, null, "0"));
		Assert.assertTrue(SwtUtils.isGtk3(true, 4523, null, "1"));
		Assert.assertTrue(SwtUtils.isGtk3(true, 4534, "3.123", null));
		Assert.assertFalse(SwtUtils.isGtk3(true, 4545, "2.123", "1"));
		Assert.assertTrue(SwtUtils.isGtk3(true, 4599, "3.123", "0"));

		Assert.assertTrue(SwtUtils.isGtk3(true, 4601, null, null));
		Assert.assertFalse(SwtUtils.isGtk3(true, 4612, null, "0"));
		Assert.assertTrue(SwtUtils.isGtk3(true, 4623, null, "1"));
		Assert.assertTrue(SwtUtils.isGtk3(true, 4634, "3.123", null));
		Assert.assertFalse(SwtUtils.isGtk3(true, 4645, "2.123", "1"));
		Assert.assertTrue(SwtUtils.isGtk3(true, 4699, "3.123", "0"));
	}

}
