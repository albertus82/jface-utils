package it.albertus.httpserver.html;

import org.junit.Assert;
import org.junit.Test;

import it.albertus.httpserver.html.HtmlUtils;

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

}
