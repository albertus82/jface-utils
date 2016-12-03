package it.albertus.jface.console;

import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;

import it.albertus.jface.SwtThreadExecutor;
import it.albertus.jface.listener.TextConsoleDisposeListener;
import it.albertus.util.Configured;
import it.albertus.util.NewLine;

public abstract class AbstractTextConsole<T extends Scrollable> extends OutputStream {

	protected static final PrintStream defaultSysOut = System.out;
	protected static final PrintStream defaultSysErr = System.err;
	protected static final String newLine = NewLine.SYSTEM_LINE_SEPARATOR;

	public static final class Defaults {
		public static final int GUI_CONSOLE_MAX_CHARS = 100000;
	}

	protected final Configured<Integer> maxChars;
	protected final T scrollable;
	protected StringBuilder buffer = new StringBuilder();

	protected AbstractTextConsole(final Composite parent, final Object layoutData, final Configured<Integer> maxChars) {
		this.maxChars = maxChars;
		scrollable = createScrollable(parent);
		scrollable.setLayoutData(layoutData);
		scrollable.setFont(JFaceResources.getTextFont());
		if (Util.isWindows()) {
			scrollable.setBackground(scrollable.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		}
		redirectStreams(); // SystemConsole & System.out will print on this SWT Text Widget. 
	}

	public T getScrollable() {
		return scrollable;
	}

	protected abstract T createScrollable(Composite parent);

	protected abstract void doPrint(String value, int maxChars);

	public abstract void clear();

	@Override
	public void write(final int b) {
		buffer.append((char) b);
		if (b == newLine.charAt(newLine.length() - 1)) {
			flush();
		}
	}

	@Override
	public void flush() {
		if (buffer.length() != 0) {
			print(buffer.toString());
			buffer = new StringBuilder();
		}
	}

	@Override
	public void close() {
		flush();
		buffer = null;
	}

	protected void print(final String value) {
		// Dealing with null argument...
		final String toPrint;
		if (value == null) {
			toPrint = String.valueOf(value);
		}
		else {
			toPrint = value;
		}

		final int maxChars = getMaxChars();

		// Actual print... (async avoids deadlocks)
		new SwtThreadExecutor(scrollable, true) {
			@Override
			protected void run() {
				doPrint(toPrint, maxChars);
			}

			@Override
			protected void onError(final Exception exception) {
				failSafePrint(toPrint);
			}
		}.start();
	}

	protected void failSafePrint(final String value) {
		defaultSysOut.print(value);
	}

	protected int getMaxChars() {
		int mc;
		try {
			mc = this.maxChars != null ? this.maxChars.getValue() : Defaults.GUI_CONSOLE_MAX_CHARS;
		}
		catch (final Exception exception) {
			mc = Defaults.GUI_CONSOLE_MAX_CHARS;
		}
		final int maxChars = mc;
		return maxChars;
	}

	protected void redirectStreams() {
		scrollable.addDisposeListener(new TextConsoleDisposeListener(defaultSysOut, defaultSysErr));
		final PrintStream ps = new PrintStream(this);
		try {
			System.setOut(ps);
			System.setErr(ps);
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
