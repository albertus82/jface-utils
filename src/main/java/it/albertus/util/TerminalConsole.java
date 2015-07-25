package it.albertus.util;

import java.io.PrintStream;

public class TerminalConsole extends Console {

	private final PrintStream out;

	private static class Singleton {
		private static final TerminalConsole console = new TerminalConsole(System.out);
	}

	public static TerminalConsole getInstance() {
		return Singleton.console;
	}

	private TerminalConsole(PrintStream printStream) {
		out = printStream;
	}

	@Override
	public void print(String value) {
		if (value == null) {
			value = String.valueOf(value);
		}
		out.print(value);
		updatePosition(value);
	}

	@Override
	public void print(char array[]) {
		out.print(array);
		updatePosition(array);
	}

	@Override
	public void println() {
		out.println();
		newLine();
	}

	@Override
	public void println(String value) {
		out.println(value);
		newLine();
	}

	@Override
	public void println(Object value) {
		out.println(value);
		newLine();
	}

	@Override
	public void println(boolean value) {
		out.println(value);
		newLine();
	}

	@Override
	public void println(char value) {
		out.println(value);
		newLine();
	}

	@Override
	public void println(int value) {
		out.println(value);
		newLine();
	}

	@Override
	public void println(long value) {
		out.println(value);
		newLine();
	}

	@Override
	public void println(float value) {
		out.println(value);
		newLine();
	}

	@Override
	public void println(double value) {
		out.println(value);
		newLine();
	}

	@Override
	public void println(char array[]) {
		out.println(array);
		newLine();
	}

}
