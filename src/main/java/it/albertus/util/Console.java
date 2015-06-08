package it.albertus.util;

import java.io.PrintStream;
import java.util.Locale;

public class Console {

	private static class Singleton {
		private static final Console console = new Console(System.out);
	}

	public static Console getInstance() {
		return Singleton.console;
	}

	private final PrintStream out;
	private int row = 0;
	private int column = 0;

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	private Console(PrintStream printStream) {
		out = printStream;
	}

	private void newLine() {
		row++;
		column = 0;
	}

	private void updatePosition(final CharSequence cs) {
		char last = '\0';
		for (int index = 0; index < cs.length(); index++) {
			final char current = cs.charAt(index);
			evaluateCharacter(last, current);
			last = current;
		}
	}

	private void updatePosition(final char[] c) {
		char last = '\0';
		for (int index = 0; index < c.length; index++) {
			final char current = c[index];
			evaluateCharacter(last, current);
			last = current;
		}
	}

	private char evaluateCharacter(final char last, final char current) {
		if (current == '\n' && last == '\r') {
			column = 0;
		}
		else if (current == '\n' || current == '\r') {
			newLine();
		}
		else if (current == '\b' && column != 0) {
			column--;
		}
		else {
			column++;
		}
		return current;
	}

	public void print(String value) {
		if (value == null) {
			value = String.valueOf(value);
		}
		out.print(value);
		updatePosition(value);
	}

	public void print(Object value) {
		print(String.valueOf(value));
	}

	public void print(boolean value) {
		print(String.valueOf(value));
	}

	public void print(char value) {
		print(String.valueOf(value));
	}

	public void print(int value) {
		print(String.valueOf(value));
	}

	public void print(long value) {
		print(String.valueOf(value));
	}

	public void print(float value) {
		print(String.valueOf(value));
	}

	public void print(double value) {
		print(String.valueOf(value));
	}

	public void print(char array[]) {
		out.print(array);
		updatePosition(array);
	}

	public void println() {
		out.println();
		newLine();
	}

	public void println(String value) {
		out.println(value);
		newLine();
	}

	public void println(Object value) {
		out.println(value);
		newLine();
	}

	public void println(boolean value) {
		out.println(value);
		newLine();
	}

	public void println(char value) {
		out.println(value);
		newLine();
	}

	public void println(int value) {
		out.println(value);
		newLine();
	}

	public void println(long value) {
		out.println(value);
		newLine();
	}

	public void println(float value) {
		out.println(value);
		newLine();
	}

	public void println(double value) {
		out.println(value);
		newLine();
	}

	public void println(char array[]) {
		out.println(array);
		newLine();
	}

	public void format(Locale l, String format, Object... args) {
		print(String.format(l, format, args));
	}

	public void format(String format, Object... args) {
		print(String.format(format, args));
	}

	public void printf(Locale l, String format, Object... args) {
		format(l, format, args);
	}

	public void printf(String format, Object... args) {
		format(format, args);
	}

	public void print(String value, boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		print(value);
	}

	public void print(Object value, boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		print(value);
	}

	public void print(boolean value, boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		print(value);
	}

	public void print(char value, boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		print(value);
	}

	public void print(int value, boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		print(value);
	}

	public void print(long value, boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		print(value);
	}

	public void print(float value, boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		print(value);
	}

	public void print(double value, boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		print(value);
	}

	public void print(char array[], boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		print(array);
	}

	public void println(String value, boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		println(value);
	}

	public void println(Object value, boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		println(value);
	}

	public void println(boolean value, boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		println(value);
	}

	public void println(char value, boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		println(value);
	}

	public void println(int value, boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		println(value);
	}

	public void println(long value, boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		println(value);
	}

	public void println(float value, boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		println(value);
	}

	public void println(double value, boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		println(value);
	}

	public void println(char array[], boolean onNewLine) {
		if (onNewLine && column != 0) {
			println();
		}
		println(array);
	}

}
