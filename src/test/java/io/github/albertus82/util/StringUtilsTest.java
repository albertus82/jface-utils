package io.github.albertus82.util;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void isEmpty() {
		Assert.assertTrue(StringUtils.isEmpty(null));
		Assert.assertTrue(StringUtils.isEmpty(""));
		Assert.assertFalse(StringUtils.isEmpty(" "));
		Assert.assertFalse(StringUtils.isEmpty("abc"));
		Assert.assertFalse(StringUtils.isEmpty("  abc  "));
	}

	@Test
	public void isNotEmpty() {
		Assert.assertFalse(StringUtils.isNotEmpty(null));
		Assert.assertFalse(StringUtils.isNotEmpty(""));
		Assert.assertTrue(StringUtils.isNotEmpty(" "));
		Assert.assertTrue(StringUtils.isNotEmpty("abc"));
		Assert.assertTrue(StringUtils.isNotEmpty("  abc  "));
	}

	@Test
	public void isBlank() {
		Assert.assertTrue(StringUtils.isBlank(null));
		Assert.assertTrue(StringUtils.isBlank(""));
		Assert.assertTrue(StringUtils.isBlank(" "));
		Assert.assertFalse(StringUtils.isBlank("abc"));
		Assert.assertFalse(StringUtils.isBlank("  abc  "));
	}

	@Test
	public void isNotBlank() {
		Assert.assertFalse(StringUtils.isNotBlank(null));
		Assert.assertFalse(StringUtils.isNotBlank(""));
		Assert.assertFalse(StringUtils.isNotBlank(" "));
		Assert.assertTrue(StringUtils.isNotBlank("abc"));
		Assert.assertTrue(StringUtils.isNotBlank("  abc  "));
	}

	@Test
	public void isNumeric() {
		Assert.assertFalse(StringUtils.isNumeric(null));
		Assert.assertFalse(StringUtils.isNumeric(""));
		Assert.assertFalse(StringUtils.isNumeric("  "));
		Assert.assertTrue(StringUtils.isNumeric("123"));
		Assert.assertFalse(StringUtils.isNumeric("12 3"));
		Assert.assertFalse(StringUtils.isNumeric("ab2c"));
		Assert.assertFalse(StringUtils.isNumeric("12-3"));
		Assert.assertFalse(StringUtils.isNumeric("12.3"));
		Assert.assertFalse(StringUtils.isNumeric("-123"));
		Assert.assertFalse(StringUtils.isNumeric("+123"));
	}

	@Test
	public void substringBefore() {
		Assert.assertNull(StringUtils.substringBefore(null, "qwerty"));
		Assert.assertEquals("", StringUtils.substringBefore("", "qwerty"));
		Assert.assertEquals("", StringUtils.substringBefore("abc", "a"));
		Assert.assertEquals("a", StringUtils.substringBefore("abcba", "b"));
		Assert.assertEquals("ab", StringUtils.substringBefore("abc", "c"));
		Assert.assertEquals("abc", StringUtils.substringBefore("abc", "d"));
		Assert.assertEquals("", StringUtils.substringBefore("abc", ""));
		Assert.assertEquals("abc", StringUtils.substringBefore("abc", null));
	}

	@Test
	public void substringAfter() {
		Assert.assertNull(StringUtils.substringAfter(null, "qwerty"));
		Assert.assertEquals("", StringUtils.substringAfter("", "qwerty"));
		Assert.assertEquals("", StringUtils.substringAfter("qwerty", null));
		Assert.assertEquals("bc", StringUtils.substringAfter("abc", "a"));
		Assert.assertEquals("cba", StringUtils.substringAfter("abcba", "b"));
		Assert.assertEquals("", StringUtils.substringAfter("abc", "c"));
		Assert.assertEquals("", StringUtils.substringAfter("abc", "d"));
		Assert.assertEquals("abc", StringUtils.substringAfter("abc", ""));
	}

	@Test
	public void trim() {
		Assert.assertNull(StringUtils.trim(null));
		Assert.assertEquals("", StringUtils.trim(""));
		Assert.assertEquals("", StringUtils.trim("     "));
		Assert.assertEquals("abc", StringUtils.trim("abc"));
		Assert.assertEquals("abc", StringUtils.trim("    abc    "));
	}

	@Test
	public void trimToEmpty() {
		Assert.assertEquals("", StringUtils.trimToEmpty(null));
		Assert.assertEquals("", StringUtils.trimToEmpty(""));
		Assert.assertEquals("", StringUtils.trimToEmpty("     "));
		Assert.assertEquals("abc", StringUtils.trimToEmpty("abc"));
		Assert.assertEquals("abc", StringUtils.trimToEmpty("    abc    "));
	}

}
