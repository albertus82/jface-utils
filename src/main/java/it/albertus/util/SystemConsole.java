package it.albertus.util;

import java.io.IOException;

public class SystemConsole extends Console {

	private static class Singleton {
		private static final SystemConsole instance = new SystemConsole();
	}

	public static SystemConsole getInstance() {
		return Singleton.instance;
	}

	@Override
	public void print(String value) {
		if (value == null) {
			value = String.valueOf(value);
		}
		System.out.print(value);
		updatePosition(value);
	}

	@Override
	public void print(char array[]) {
		System.out.print(array);
		updatePosition(array);
	}

	@Override
	public void println() {
		System.out.println();
		newLine();
	}

	@Override
	public void println(String value) {
		System.out.println(value);
		newLine();
	}

	@Override
	public void println(Object value) {
		System.out.println(value);
		newLine();
	}

	@Override
	public void println(boolean value) {
		System.out.println(value);
		newLine();
	}

	@Override
	public void println(char value) {
		System.out.println(value);
		newLine();
	}

	@Override
	public void println(int value) {
		System.out.println(value);
		newLine();
	}

	@Override
	public void println(long value) {
		System.out.println(value);
		newLine();
	}

	@Override
	public void println(float value) {
		System.out.println(value);
		newLine();
	}

	@Override
	public void println(double value) {
		System.out.println(value);
		newLine();
	}

	@Override
	public void println(char array[]) {
		System.out.println(array);
		newLine();
	}

	@Override
	public void write(int b) throws IOException {
		System.out.write(b);
	}

}
