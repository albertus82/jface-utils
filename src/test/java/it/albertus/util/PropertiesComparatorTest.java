package it.albertus.util;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.albertus.util.PropertiesComparator.CompareResults;

public class PropertiesComparatorTest {

	private static Properties a;
	private static Properties b;

	@Before
	public void before() {
		a = new Properties();
		b = new Properties();
		for (char c = 'a'; c <= 'z'; c++) {
			a.setProperty(Character.toString(c), Integer.toString(c - 'a' + 1));
			b.setProperty(Character.toString(c), Integer.toString(c - 'a' + 1));
		}
	}

	@Test
	public void equalsTest() {
		final CompareResults results = PropertiesComparator.compare(a, b);
		Assert.assertTrue(results.getDifferentValues().isEmpty());
		Assert.assertTrue(results.getLeftOnly().isEmpty());
		Assert.assertTrue(results.getRightOnly().isEmpty());
	}

	@Test
	public void removedLeftTest() {
		final int size = a.size();
		final byte end = (byte) (Math.random() * size + 1);
		final Set<String> keysToRemove = new HashSet<String>();
		for (byte i = 0; i < end; i++) {
			keysToRemove.add(Character.toString((char) (Math.random() * size + 'a')));
		}
		System.out.println("removedLeftTest - keysToRemove: " + keysToRemove);
		for (final String key : keysToRemove) {
			a.remove(key);
		}
		final CompareResults results = PropertiesComparator.compare(a, b);
		Assert.assertTrue(results.getDifferentValues().isEmpty());
		Assert.assertTrue(results.getLeftOnly().isEmpty());
		Assert.assertEquals(keysToRemove.size(), results.getRightOnly().size());
		for (final String key : keysToRemove) {
			Assert.assertTrue(results.getRightOnly().containsKey(key));
			Assert.assertFalse(results.getLeftOnly().containsKey(key));
		}
	}

	@Test
	public void removedRightTest() {
		final int size = b.size();
		final byte end = (byte) (Math.random() * size + 1);
		final Set<String> keysToRemove = new HashSet<String>();
		for (byte i = 0; i < end; i++) {
			keysToRemove.add(Character.toString((char) (Math.random() * size + 'a')));
		}
		System.out.println("removedRightTest - keysToRemove: " + keysToRemove);
		for (final String key : keysToRemove) {
			b.remove(key);
		}
		final CompareResults results = PropertiesComparator.compare(a, b);
		Assert.assertTrue(results.getDifferentValues().isEmpty());
		Assert.assertTrue(results.getRightOnly().isEmpty());
		Assert.assertEquals(keysToRemove.size(), results.getLeftOnly().size());
		for (final String key : keysToRemove) {
			Assert.assertFalse(results.getRightOnly().containsKey(key));
			Assert.assertTrue(results.getLeftOnly().containsKey(key));
		}
	}

	@Test
	public void changedLeftTest() {
		final int size = a.size();
		final byte end = (byte) (Math.random() * size + 1);
		final Set<String> keysToModify = new HashSet<String>();
		for (byte i = 0; i < end; i++) {
			keysToModify.add(Character.toString((char) (Math.random() * size + 'a')));
		}
		System.out.println("changedLeftTest - keysToModify: " + keysToModify);
		for (final String key : keysToModify) {
			a.setProperty(key, "asdfghjkl");
		}
		final CompareResults results = PropertiesComparator.compare(a, b);
		Assert.assertTrue(results.getLeftOnly().isEmpty());
		Assert.assertTrue(results.getRightOnly().isEmpty());
		Assert.assertEquals(keysToModify.size(), results.getDifferentValues().size());
		for (final String key : keysToModify) {
			Assert.assertEquals(Integer.toString(key.charAt(0) - 'a' + 1), results.getDifferentValues().get(key).get(1));
			Assert.assertEquals("asdfghjkl", results.getDifferentValues().get(key).get(0));
		}
	}

	@Test
	public void changedRightTest() {
		final int size = b.size();
		final byte end = (byte) (Math.random() * size + 1);
		final Set<String> keysToModify = new HashSet<String>();
		for (byte i = 0; i < end; i++) {
			keysToModify.add(Character.toString((char) (Math.random() * size + 'a')));
		}
		System.out.println("changedRightTest - keysToModify: " + keysToModify);
		for (final String key : keysToModify) {
			b.setProperty(key, "qwertyuiop");
		}
		final CompareResults results = PropertiesComparator.compare(a, b);
		Assert.assertTrue(results.getLeftOnly().isEmpty());
		Assert.assertTrue(results.getRightOnly().isEmpty());
		Assert.assertEquals(keysToModify.size(), results.getDifferentValues().size());
		for (final String key : keysToModify) {
			Assert.assertEquals(Integer.toString(key.charAt(0) - 'a' + 1), results.getDifferentValues().get(key).get(0));
			Assert.assertEquals("qwertyuiop", results.getDifferentValues().get(key).get(1));
		}
	}

}
