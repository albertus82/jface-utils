package it.albertus.util.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import it.albertus.util.ConfigurationException;
import it.albertus.util.IOUtils;
import it.albertus.util.SystemUtils;

public class ConfigurationTest {

	private static File propertiesFile;
	private static Configuration configuration;

	@BeforeClass
	public static void init() throws IOException {
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			propertiesFile = File.createTempFile("configuration-", ".cfg");
			fw = new FileWriter(propertiesFile);
			bw = new BufferedWriter(fw);

			bw.write("bigdecimal.ok=12345678901234567890.24680135792468013579  ");
			bw.newLine();
			bw.write("bigdecimal.ko= r34hf 34895");
			bw.newLine();
			bw.write("bigdecimal.null.1=");
			bw.newLine();
			bw.write("bigdecimal.null.2=     ");
			bw.newLine();
			bw.write("biginteger.ok= 123456789012345678902468013579");
			bw.newLine();
			bw.write("biginteger.ko=10000000d");
			bw.newLine();
			bw.write("biginteger.null.1=");
			bw.newLine();
			bw.write("biginteger.null.2=     ");
			bw.newLine();
			bw.write("long.ok=123456789012345     \t");
			bw.newLine();
			bw.write("long.ko=abcdefghijklmno");
			bw.newLine();
			bw.write("long.null.1=");
			bw.newLine();
			bw.write("long.null.2=     ");
			bw.newLine();
			bw.write("int.ok= -1234567890");
			bw.newLine();
			bw.write("int.ko=12345678901");
			bw.newLine();
			bw.write("int.null.1=\t\t\t");
			bw.newLine();
			bw.write("int.null.2=");
			bw.newLine();
			bw.write("byte.ok=127");
			bw.newLine();
			bw.write("byte.ko=128");
			bw.newLine();
			bw.write("byte.null.1=");
			bw.newLine();
			bw.write("byte.null.2=  \t  ");
			bw.newLine();
			bw.write("short.ok = -12345");
			bw.newLine();
			bw.write("short.ko=12ab5");
			bw.newLine();
			bw.write("short.null.1= \t \t");
			bw.newLine();
			bw.write("short.null.2=");
			bw.newLine();
			bw.write("float.ok=123.456");
			bw.newLine();
			bw.write("float.ko=12x.789");
			bw.newLine();
			bw.write("float.null.1=");
			bw.newLine();
			bw.write("float.null.2=\t");
			bw.newLine();
			bw.write("double.ok=1e38");
			bw.newLine();
			bw.write("double.ko=2eee55");
			bw.newLine();
			bw.write("double.null.1= ");
			bw.newLine();
			bw.write("double.null.2=");
			bw.newLine();
			bw.write("char.ok.1=X");
			bw.newLine();
			bw.write("char.ok.2=\\ ");
			bw.newLine();
			bw.write("char.ko=YY");
			bw.newLine();
			bw.write("char.null.1=");
			bw.newLine();
			bw.write("char.null.2= ");
			bw.newLine();
			bw.write("boolean.true.1=TRUE");
			bw.newLine();
			bw.write("boolean.true.2=true");
			bw.newLine();
			bw.write("boolean.true.3=   TrUe  ");
			bw.newLine();
			bw.write("boolean.true.4=1");
			bw.newLine();
			bw.write("boolean.true.5=\t1\t    ");
			bw.newLine();
			bw.write("boolean.false.1=FALSE");
			bw.newLine();
			bw.write("boolean.false.2=false");
			bw.newLine();
			bw.write("boolean.false.3=     FaLsE     ");
			bw.newLine();
			bw.write("boolean.false.4=0");
			bw.newLine();
			bw.write("boolean.false.5=  0      ");
			bw.newLine();
			bw.write("boolean.null.1=  ");
			bw.newLine();
			bw.write("boolean.null.2=");
			bw.newLine();
			bw.write("string.empty=");
			bw.newLine();
			bw.write("string=Qwertyuiop Asdfghjkl Zxcvbnm");
			bw.newLine();
			bw.write("test.password=TestPassword12345");
			bw.newLine();
		}
		finally {
			IOUtils.closeQuietly(bw, fw);
		}
		System.out.println(propertiesFile);
		for (int i = 0; i < propertiesFile.toString().length(); i++) {
			System.out.print('-');
		}
		System.out.println();
		final Properties properties = new Properties();
		FileReader fr = null;
		try {
			fr = new FileReader(propertiesFile);
			properties.load(fr);
		}
		finally {
			IOUtils.closeQuietly(fr);
		}
		final Map<String, String> entries = new TreeMap<String, String>();
		for (final Object key : properties.keySet()) {
			entries.put((String) key, properties.getProperty((String) key));
		}
		for (final Entry<String, String> entry : entries.entrySet()) {
			System.out.println(entry.getKey() + '=' + entry.getValue());
		}
		System.out.println("----------------------------------------------");

		configuration = new Configuration(new PropertiesConfiguration(propertiesFile.getPath()));
		System.out.println(configuration.getClass().getName() + ' ' + configuration);
	}

	@Test
	public void testConstructors() throws IOException {
		Assert.assertNotNull(new Configuration(new PropertiesConfiguration(propertiesFile.getPath(), true)));
		Assert.assertNotNull(new Configuration(new PropertiesConfiguration(propertiesFile.getPath(), false)));
	}

	@Test
	public void testToString() {
		final String string = configuration.toString();
		Assert.assertNotNull(string);
		Assert.assertNotEquals("", string);
		Assert.assertFalse(string.contains("TestPassword12345"));
		Assert.assertTrue(string.contains("test.password=" + PropertiesConfiguration.PASSWORD_PLACEHOLDER));
	}

	@Test
	public void testStaticMethods() {
		String str = SystemUtils.getOsSpecificConfigurationDir();
		Assert.assertNotNull(str);
		File file = new File(str);
		Assert.assertNotNull(file);
		System.out.println(file);

		str = SystemUtils.getOsSpecificDocumentsDir();
		Assert.assertNotNull(str);
		file = new File(str);
		Assert.assertNotNull(file);
		System.out.println(file);

		str = SystemUtils.getOsSpecificLocalAppDataDir();
		Assert.assertNotNull(str);
		file = new File(str);
		Assert.assertNotNull(file);
		System.out.println(file);
	}

	@Test
	public void testOtherMethods() {
		Assert.assertTrue(configuration.contains("bigdecimal.ok"));
		Assert.assertFalse(configuration.contains("Non-existent key"));
		Assert.assertNotNull(configuration.getProperties());
	}

	@Test
	public void getLong() {
		Assert.assertNull(configuration.getLong("Non-existent key"));
		final String type = "long";
		Assert.assertEquals(Long.valueOf(123456789012345L), configuration.getLong(type + ".ok"));
		Assert.assertEquals(Long.valueOf(246801357924680L), Long.valueOf(configuration.getLong(type + ".null.1", 246801357924680L)));
		Assert.assertEquals(Long.valueOf(135792468013579L), Long.valueOf(configuration.getLong(type + ".ko", 135792468013579L)));
		Assert.assertNull(configuration.getLong(type + ".null.2"));
		try {
			configuration.getLong(type + ".ko");
			Assert.assertTrue(false);
		}
		catch (final ConfigurationException ce) {
			System.err.println(ce);
			Assert.assertTrue(true);
		}
		catch (final Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void getInt() {
		Assert.assertNull(configuration.getInt("Non-existent key"));
		final String type = "int";
		Assert.assertEquals(Integer.valueOf(-1234567890), configuration.getInt(type + ".ok"));
		Assert.assertEquals(Integer.valueOf(24680), Integer.valueOf(configuration.getInt(type + ".null.1", 24680)));
		Assert.assertEquals(Integer.valueOf(13579), Integer.valueOf(configuration.getInt(type + ".ko", 13579)));
		Assert.assertNull(configuration.getLong(type + ".null.2"));
		try {
			configuration.getInt(type + ".ko");
			Assert.assertTrue(false);
		}
		catch (final ConfigurationException ce) {
			System.err.println(ce);
			Assert.assertTrue(true);
		}
		catch (final Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void getShort() {
		Assert.assertNull(configuration.getShort("Non-existent key"));
		final String type = "short";
		Assert.assertEquals(Short.valueOf((short) -12345), configuration.getShort(type + ".ok"));
		Assert.assertEquals(Short.valueOf((short) 13579), Short.valueOf(configuration.getShort(type + ".null.1", (short) 13579)));
		Assert.assertEquals(Short.valueOf((short) 24680), Short.valueOf(configuration.getShort(type + ".ko", (short) 24680)));
		Assert.assertNull(configuration.getShort(type + ".null.2"));
		try {
			configuration.getShort(type + ".ko");
			Assert.assertTrue(false);
		}
		catch (final ConfigurationException ce) {
			System.err.println(ce);
			Assert.assertTrue(true);
		}
		catch (final Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void getByte() {
		Assert.assertNull(configuration.getByte("Non-existent key"));
		final String type = "byte";
		Assert.assertEquals(Byte.valueOf((byte) 127), configuration.getByte(type + ".ok"));
		Assert.assertEquals(Byte.valueOf((byte) -100), Byte.valueOf(configuration.getByte(type + ".null.1", (byte) -100)));
		Assert.assertEquals(Byte.valueOf((byte) 50), Byte.valueOf(configuration.getByte(type + ".ko", (byte) 50)));
		Assert.assertNull(configuration.getByte(type + ".null.2"));
		try {
			configuration.getByte(type + ".ko");
			Assert.assertTrue(false);
		}
		catch (final ConfigurationException ce) {
			System.err.println(ce);
			Assert.assertTrue(true);
		}
		catch (final Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void getFloat() {
		Assert.assertNull(configuration.getFloat("Non-existent key"));
		final String type = "float";
		Assert.assertEquals(Float.valueOf(123.456f), configuration.getFloat(type + ".ok"));
		Assert.assertEquals(Float.valueOf(-246.80135f), Float.valueOf(configuration.getFloat(type + ".null.1", -246.80135f)));
		Assert.assertEquals(Float.valueOf(-135.79246f), Float.valueOf(configuration.getFloat(type + ".ko", -135.79246f)));
		Assert.assertNull(configuration.getFloat(type + ".null.2"));
		try {
			configuration.getFloat(type + ".ko");
			Assert.assertTrue(false);
		}
		catch (final ConfigurationException ce) {
			System.err.println(ce);
			Assert.assertTrue(true);
		}
		catch (final Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void getDouble() {
		Assert.assertNull(configuration.getDouble("Non-existent key"));
		final String type = "double";
		Assert.assertEquals(Double.valueOf(1e38), configuration.getDouble(type + ".ok"));
		Assert.assertEquals(Double.valueOf(-13579.24680), Double.valueOf(configuration.getDouble(type + ".null.1", -13579.24680)));
		Assert.assertEquals(Double.valueOf(-24680.13579), Double.valueOf(configuration.getDouble(type + ".ko", -24680.13579)));
		Assert.assertNull(configuration.getDouble(type + ".null.2"));
		try {
			configuration.getDouble(type + ".ko");
			Assert.assertTrue(false);
		}
		catch (final ConfigurationException ce) {
			System.err.println(ce);
			Assert.assertTrue(true);
		}
		catch (final Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void getBigDecimal() {
		Assert.assertNull(configuration.getBigDecimal("Non-existent key"));
		final String type = "bigdecimal";
		Assert.assertEquals(new BigDecimal("12345678901234567890.24680135792468013579"), configuration.getBigDecimal(type + ".ok"));
		Assert.assertEquals(new BigDecimal("1122334455.6677889900"), configuration.getBigDecimal(type + ".null.1", new BigDecimal("1122334455.6677889900")));
		Assert.assertEquals(new BigDecimal(1234567890123456789L), configuration.getBigDecimal(type + ".ko", BigDecimal.valueOf(1234567890123456789L)));
		Assert.assertNull(configuration.getBigDecimal(type + ".null.2"));
		try {
			configuration.getBigDecimal(type + ".ko");
			Assert.assertTrue(false);
		}
		catch (final ConfigurationException ce) {
			System.err.println(ce);
			Assert.assertTrue(true);
		}
		catch (final Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void getBigInteger() {
		Assert.assertNull(configuration.getBigInteger("Non-existent key"));
		final String type = "biginteger";
		Assert.assertEquals(new BigInteger("123456789012345678902468013579"), configuration.getBigInteger(type + ".ok"));
		Assert.assertEquals(new BigInteger("11223344556677889900123456789012345678902468013579"), configuration.getBigInteger(type + ".null.1", new BigInteger("11223344556677889900123456789012345678902468013579")));
		Assert.assertEquals(new BigInteger("12345678901234567890123456789012345678902468013579"), configuration.getBigInteger(type + ".ko", new BigInteger("12345678901234567890123456789012345678902468013579")));
		Assert.assertNull(configuration.getBigInteger(type + ".null.2"));
		try {
			configuration.getBigInteger(type + ".ko");
			Assert.assertTrue(false);
		}
		catch (final ConfigurationException ce) {
			System.err.println(ce);
			Assert.assertTrue(true);
		}
		catch (final Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void getChar() {
		Assert.assertNull(configuration.getChar("Non-existent key"));
		final String type = "char";
		Assert.assertEquals(Character.valueOf('X'), configuration.getChar(type + ".ok.1"));
		Assert.assertEquals(Character.valueOf(' '), configuration.getChar(type + ".ok.2"));
		Assert.assertEquals(Character.valueOf('Z'), Character.valueOf(configuration.getChar(type + ".null.1", 'Z')));
		Assert.assertEquals(Character.valueOf('Y'), Character.valueOf(configuration.getChar(type + ".ko", 'Y')));
		Assert.assertNull(configuration.getChar(type + ".null.2"));
		try {
			configuration.getChar(type + ".ko");
			Assert.assertTrue(false);
		}
		catch (final ConfigurationException ce) {
			System.err.println(ce);
			Assert.assertTrue(true);
		}
		catch (final Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void getBoolean() {
		Assert.assertNull(configuration.getBoolean("Non-existent key"));
		Assert.assertTrue(configuration.getBoolean("Non-existent key", true));
		Assert.assertFalse(configuration.getBoolean("Non-existent key", false));
		Assert.assertFalse(configuration.getBoolean("double.ok")); // invalid boolean string -> false
		Assert.assertFalse(configuration.getBoolean("double.ok", true)); // invalid boolean string -> false
		final String type = "boolean";
		for (int i = 1; i <= 5; i++) {
			Assert.assertTrue(configuration.getBoolean(type + ".true." + i));
			Assert.assertFalse(configuration.getBoolean(type + ".false." + i));
		}
		for (int i = 1; i <= 2; i++) {
			Assert.assertNull(configuration.getBoolean(type + ".null." + i));
		}
	}

	@Test
	public void getString() {
		Assert.assertNull(configuration.getString("Non-existent key"));
		Assert.assertEquals("", configuration.getString("string.empty"));
		Assert.assertEquals("", configuration.getString("string.empty", ""));
		Assert.assertEquals("", configuration.getString("string.empty", true));
		Assert.assertEquals("", configuration.getString("string.null", true));
		Assert.assertNull(configuration.getString("string.null", false));
		Assert.assertNull(configuration.getString("string.null"));
	}

	@Test
	public void getCharArray() {
		Assert.assertNull(configuration.getCharArray("Non-existent key"));
		Assert.assertArrayEquals(new char[] {}, configuration.getCharArray("string.empty"));
		Assert.assertArrayEquals("Qwertyuiop Asdfghjkl Zxcvbnm".toCharArray(), configuration.getCharArray("string"));
	}

	@AfterClass
	public static void destroy() {
		if (propertiesFile != null) {
			if (propertiesFile.delete()) {
				System.out.println("Deleted configuration file \"" + propertiesFile + '"');
			}
			else {
				System.err.println("Cannot delete configuration file \"" + propertiesFile + '"');
				propertiesFile.deleteOnExit();
			}
		}
	}

}
