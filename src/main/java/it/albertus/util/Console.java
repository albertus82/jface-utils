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

	private void updatePosition(CharSequence cs) {
		Character last = null;
		for (int index = 0; index < cs.length(); index++) {
			last = evaluateCharacter(last, cs.charAt(index));
		}
	}

	private void updatePosition(char[] c) {
		Character last = null;
		for (int index = 0; index < c.length; index++) {
			last = evaluateCharacter(last, c[index]);
		}
	}

	private char evaluateCharacter(Character last, char current) {
		if (current == '\r' || (current == '\n' && last != null && last != '\r')) {
			newLine();
		}
		else if (current == '\b' && column != 0) {
			column--;
		}
		else {
			column++;
		}
		last = current;
		return last;
	}

	public void print(String s) {
		if (s == null) {
			s = String.valueOf(s);
		}
		out.print(s);
		updatePosition(s);
	}

	public void print(Object o) {
		print(String.valueOf(o));
	}

	public void print(boolean b) {
		print(String.valueOf(b));
	}

	public void print(char c) {
		print(String.valueOf(c));
	}

	public void print(int i) {
		print(String.valueOf(i));
	}

	public void print(long l) {
		print(String.valueOf(l));
	}

	public void print(float f) {
		print(String.valueOf(f));
	}

	public void print(double d) {
		print(String.valueOf(d));
	}

	public void print(char s[]) {
		out.print(s);
		updatePosition(s);
	}

	public void println() {
		out.println();
		newLine();
	}

	public void println(String s) {
		out.println(s);
		newLine();
	}

	public void println(Object o) {
		out.println(o);
		newLine();
	}

	public void println(boolean b) {
		out.println(b);
		newLine();
	}

	public void println(char c) {
		out.println(c);
		newLine();
	}

	public void println(int i) {
		out.println(i);
		newLine();
	}

	public void println(long l) {
		out.println(l);
		newLine();
	}

	public void println(float f) {
		out.println(f);
		newLine();
	}

	public void println(double d) {
		out.println(d);
		newLine();
	}

	public void println(char c[]) {
		out.println(c);
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

	public void printOnNewLine(String s) {
		if (column != 0) {
			println();
		}
		print(s);
	}

	public void printOnNewLine(Object o) {
		if (column != 0) {
			println();
		}
		print(o);
	}

	public void printOnNewLine(boolean b) {
		if (column != 0) {
			println();
		}
		print(b);
	}

	public void printOnNewLine(char c) {
		if (column != 0) {
			println();
		}
		print(c);
	}

	public void printOnNewLine(int i) {
		if (column != 0) {
			println();
		}
		print(i);
	}

	public void printOnNewLine(long l) {
		if (column != 0) {
			println();
		}
		print(l);
	}

	public void printOnNewLine(float f) {
		if (column != 0) {
			println();
		}
		print(f);
	}

	public void printOnNewLine(double d) {
		if (column != 0) {
			println();
		}
		print(d);
	}

	public void printOnNewLine(char c[]) {
		if (column != 0) {
			println();
		}
		print(c);
	}

	public void printlnOnNewLine(String s) {
		if (column != 0) {
			println();
		}
		println(s);
	}

	public void printlnOnNewLine(Object o) {
		if (column != 0) {
			println();
		}
		println(o);
	}

	public void printlnOnNewLine(boolean b) {
		if (column != 0) {
			println();
		}
		println(b);
	}

	public void printlnOnNewLine(char c) {
		if (column != 0) {
			println();
		}
		println(c);
	}

	public void printlnOnNewLine(int i) {
		if (column != 0) {
			println();
		}
		println(i);
	}

	public void printlnOnNewLine(long l) {
		if (column != 0) {
			println();
		}
		println(l);
	}

	public void printlnOnNewLine(float f) {
		if (column != 0) {
			println();
		}
		println(f);
	}

	public void printlnOnNewLine(double d) {
		if (column != 0) {
			println();
		}
		println(d);
	}

	public void printlnOnNewLine(char c[]) {
		if (column != 0) {
			println();
		}
		println(c);
	}

}
