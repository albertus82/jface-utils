package it.albertus.util;

public class SystemConsole extends Console {

	private static class Singleton {
		private static final SystemConsole instance = new SystemConsole();

		private Singleton() {
			throw new IllegalAccessError();
		}
	}

	protected SystemConsole() {}

	public static SystemConsole getInstance() {
		return Singleton.instance;
	}

	@Override
	public void print(final String value) {
		System.out.print(value);
		updatePosition(value);
	}

	@Override
	public void print(final char[] array) {
		System.out.print(array);
		updatePosition(array);
	}

	@Override
	public void println() {
		System.out.println();
		newLine();
	}

	@Override
	public void println(final String value) {
		System.out.println(value);
		newLine();
	}

	@Override
	public void println(final Object value) {
		System.out.println(value);
		newLine();
	}

	@Override
	public void println(final boolean value) {
		System.out.println(value);
		newLine();
	}

	@Override
	public void println(final char value) {
		System.out.println(value);
		newLine();
	}

	@Override
	public void println(final int value) {
		System.out.println(value);
		newLine();
	}

	@Override
	public void println(final long value) {
		System.out.println(value);
		newLine();
	}

	@Override
	public void println(final float value) {
		System.out.println(value);
		newLine();
	}

	@Override
	public void println(final double value) {
		System.out.println(value);
		newLine();
	}

	@Override
	public void println(final char[] array) {
		System.out.println(array);
		newLine();
	}

}
