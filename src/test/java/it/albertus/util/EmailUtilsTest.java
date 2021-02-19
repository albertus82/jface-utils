package it.albertus.util;

import org.junit.Assert;
import org.junit.Test;

public class EmailUtilsTest {

	@Test
	public void testValidateAddressOK() {
		Assert.assertTrue(EmailUtils.validateAddress("prettyandsimple@example.com"));
		Assert.assertTrue(EmailUtils.validateAddress("very.common@example.com"));
		Assert.assertTrue(EmailUtils.validateAddress("disposable.style.email.with+symbol@example.com"));
		Assert.assertTrue(EmailUtils.validateAddress("other.email-with-dash@example.com"));
		Assert.assertTrue(EmailUtils.validateAddress("x@example.com"));
		Assert.assertTrue(EmailUtils.validateAddress("\"much.more unusual\"@example.com"));
		Assert.assertTrue(EmailUtils.validateAddress("\"very.unusual.@.unusual.com\"@example.com"));
		Assert.assertTrue(EmailUtils.validateAddress("\"very.(),:;<>[]\\\".VERY.\\\"very@\\ \\\"very\\\".unusual\"@strange.example.com"));
		Assert.assertTrue(EmailUtils.validateAddress("example-indeed@strange-example.com"));
		Assert.assertTrue(EmailUtils.validateAddress("admin@mailserver1"));
		Assert.assertTrue(EmailUtils.validateAddress("#!$%&'*+-/=?^_`{}|~@example.org"));
		Assert.assertTrue(EmailUtils.validateAddress("\"()<>[]:,;@\\\\\"!#$%&'-/=?^_`{}| ~.a\"@example.org"));
		Assert.assertTrue(EmailUtils.validateAddress("\" \"@example.org"));
		Assert.assertTrue(EmailUtils.validateAddress("example@localhost"));
		Assert.assertTrue(EmailUtils.validateAddress("example@s.solutions"));
		Assert.assertTrue(EmailUtils.validateAddress("user@com"));
		Assert.assertTrue(EmailUtils.validateAddress("user@localserver"));
		Assert.assertTrue(EmailUtils.validateAddress("user@[IPv6:2001:db8::1]"));
		Assert.assertTrue(EmailUtils.validateAddress("\"Abc\\@def\"@example.com"));
		Assert.assertTrue(EmailUtils.validateAddress("\"Fred Bloggs\"@example.com"));
		Assert.assertTrue(EmailUtils.validateAddress("\"Joe\\\\Blow\"@example.com"));
		Assert.assertTrue(EmailUtils.validateAddress("\"Abc@def\"@example.com"));
		Assert.assertTrue(EmailUtils.validateAddress("customer/department=shipping@example.com"));
		Assert.assertTrue(EmailUtils.validateAddress("\\$A12345@example.com"));
		Assert.assertTrue(EmailUtils.validateAddress("!def!xyz%abc@example.com"));
		Assert.assertTrue(EmailUtils.validateAddress("_somename@example.com"));
	}

	@Test
	public void testValidateAddressKO() {
		Assert.assertFalse(EmailUtils.validateAddress(null));
		Assert.assertFalse(EmailUtils.validateAddress(""));
		Assert.assertFalse(EmailUtils.validateAddress(" "));
		Assert.assertFalse(EmailUtils.validateAddress("Abc.example.com"));
		Assert.assertFalse(EmailUtils.validateAddress("A@b@c@example.com"));
		Assert.assertFalse(EmailUtils.validateAddress("john.doe@example..com"));
		Assert.assertFalse(EmailUtils.validateAddress(" prettyandsimple@example.com"));
		Assert.assertFalse(EmailUtils.validateAddress("  prettyandsimple@example.com"));
		Assert.assertFalse(EmailUtils.validateAddress("prettyandsimple@example.com "));
		Assert.assertFalse(EmailUtils.validateAddress("prettyandsimple@example.com  "));
		Assert.assertFalse(EmailUtils.validateAddress(" prettyandsimple@example.com "));
		Assert.assertFalse(EmailUtils.validateAddress("  prettyandsimple@example.com  "));
		Assert.assertFalse(EmailUtils.validateAddress("prettyandsimple@ example.com"));
		Assert.assertFalse(EmailUtils.validateAddress("prettyandsimple @example.com"));
		Assert.assertFalse(EmailUtils.validateAddress("prettyandsimple @ example.com"));
	}

}
