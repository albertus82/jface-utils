package it.albertus.util;

import java.util.Locale;

public abstract class Console {

	protected int row = 0;
	protected int column = 0;

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	protected void newLine() {
		row++;
		column = 0;
	}

	protected void updatePosition(final CharSequence cs) {
		char last = '\0';
		for (int index = 0; index < cs.length(); index++) {
			final char current = cs.charAt(index);
			evaluateCharacter(last, current);
			last = current;
		}
	}

	protected void updatePosition(final char[] c) {
		char last = '\0';
		for (int index = 0; index < c.length; index++) {
			final char current = c[index];
			evaluateCharacter(last, current);
			last = current;
		}
	}

	protected char evaluateCharacter(final char last, final char current) {
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

	public abstract void print(String value);

	public abstract void print(Object value);

	public abstract void print(boolean value);

	public abstract void print(char value);

	public abstract void print(int value);

	public abstract void print(long value);

	public abstract void print(float value);

	public abstract void print(double value);

	public abstract void print(char array[]);

	public abstract void println();

	public abstract void println(String value);

	public abstract void println(Object value);

	public abstract void println(boolean value);

	public abstract void println(char value);

	public abstract void println(int value);

	public abstract void println(long value);

	public abstract void println(float value);

	public abstract void println(double value);

	public abstract void println(char array[]);

	public abstract void format(Locale l, String format, Object... args);

	public abstract void format(String format, Object... args);

	public abstract void printf(Locale l, String format, Object... args);

	public abstract void printf(String format, Object... args);

	public abstract void print(String value, boolean onNewLine);

	public abstract void print(Object value, boolean onNewLine);

	public abstract void print(boolean value, boolean onNewLine);

	public abstract void print(char value, boolean onNewLine);

	public abstract void print(int value, boolean onNewLine);

	public abstract void print(long value, boolean onNewLine);

	public abstract void print(float value, boolean onNewLine);

	public abstract void print(double value, boolean onNewLine);

	public abstract void print(char array[], boolean onNewLine);

	public abstract void println(String value, boolean onNewLine);

	public abstract void println(Object value, boolean onNewLine);

	public abstract void println(boolean value, boolean onNewLine);

	public abstract void println(char value, boolean onNewLine);

	public abstract void println(int value, boolean onNewLine);

	public abstract void println(long value, boolean onNewLine);

	public abstract void println(float value, boolean onNewLine);

	public abstract void println(double value, boolean onNewLine);

	public abstract void println(char array[], boolean onNewLine);

}
