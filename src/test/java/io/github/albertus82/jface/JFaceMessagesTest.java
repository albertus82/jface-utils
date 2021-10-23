package io.github.albertus82.jface;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

public class JFaceMessagesTest {

	@Test
	public void test1() {
		JFaceMessages.setLanguage(Locale.ENGLISH.getLanguage());
		Assert.assertEquals("OK", JFaceMessages.get("lbl.button.ok").toUpperCase());
	}

	@Test
	public void test2() {
		JFaceMessages.setLanguage(Locale.ENGLISH.getLanguage());
		Assert.assertEquals(JFaceMessages.get("lbl.button.ok", "a", "b", 3, 4), JFaceMessages.get("lbl.button.ok"));
	}

	@Test
	public void test3() {
		JFaceMessages.setLanguage(Locale.ITALIAN.getLanguage());
		Assert.assertEquals(JFaceMessages.get("lbl.preferences.header.restart", "a", "b", 3, 4), JFaceMessages.get("lbl.preferences.header.restart"));
	}

	@Test
	public void test4() {
		JFaceMessages.setLanguage(Locale.ITALIAN.getLanguage());
		Assert.assertEquals(JFaceMessages.get("lbl.preferences.logging.overridden", "a", "b", 3, 4), JFaceMessages.get("lbl.preferences.logging.overridden"));
	}

}
