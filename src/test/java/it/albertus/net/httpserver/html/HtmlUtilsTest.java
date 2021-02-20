package it.albertus.net.httpserver.html;

import org.junit.Assert;
import org.junit.Test;

public class HtmlUtilsTest {

	private static final String petrarca = "e 'l viso di pietosi color farsi,\r\nnon so se vero o falso, mi parea:\r\ni' che l'esca amorosa al petto avea,\r\nqual meraviglia se di subito arsi?";
	private static final String specials = "'qwertyuiop' & <<asdfghjkl>> && \"\"\"zxcvbnm\"\"\"";

	@Test
	public void escapeHtmlTest() {
		String escaped = HtmlUtils.escapeHtml(petrarca);
		System.out.println(escaped);
		Assert.assertEquals(petrarca, HtmlUtils.unescapeHtml(escaped));

		escaped = HtmlUtils.escapeHtml(specials);
		System.out.println(escaped);
		Assert.assertEquals(specials, HtmlUtils.unescapeHtml(escaped));
	}

	@Test
	public void escapeEcmaScriptTest() {
		Assert.assertEquals("\\u20AC qwertyuiop \\x80 \\x27asdfghjkl\\x27 \\x3C\\x22zxcvbnm\\x22\\x3E \\x26\\\\\\x2F", HtmlUtils.escapeEcmaScript("\u20ac qwertyuiop \u0080 'asdfghjkl' <\"zxcvbnm\"> &\\/"));
	}

}
