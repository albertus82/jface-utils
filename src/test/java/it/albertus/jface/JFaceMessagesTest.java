package it.albertus.jface;

import java.util.Locale;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class JFaceMessagesTest {

	@BeforeClass
	public static void beforeClass() {
		JFaceMessages.setLanguage(Locale.ENGLISH.getLanguage());
	}

	@Test
	public void test1() {
		Assert.assertEquals("OK", JFaceMessages.get("lbl.button.ok").toUpperCase());
	}

	@Test
	public void test2() {
		Assert.assertEquals(JFaceMessages.get("lbl.button.ok", "a", "b", 3, 4), JFaceMessages.get("lbl.button.ok"));
	}

}
