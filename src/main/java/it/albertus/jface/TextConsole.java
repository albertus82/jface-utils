package it.albertus.jface;

import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Text;

import it.albertus.jface.listener.TextConsoleDisposeListener;
import it.albertus.util.Configured;
import it.albertus.util.NewLine;

public class TextConsole extends OutputStream {

	protected static final PrintStream defaultSysOut = System.out;
	protected static final PrintStream defaultSysErr = System.err;
	protected static final String newLine = NewLine.SYSTEM_LINE_SEPARATOR;

	public interface Defaults {
		int GUI_CONSOLE_MAX_CHARS = 100000;
	}

	protected final Configured<Integer> maxChars;
	protected final Scrollable scrollable;
	protected StringBuilder buffer = new StringBuilder();

	public TextConsole(final Composite parent, final Object layoutData) {
		this(parent, layoutData, null);
	}

	public TextConsole(final Composite parent, final Object layoutData, final Configured<Integer> maxChars) {
		this.maxChars = maxChars;
		this.scrollable = createText(parent);
		scrollable.setLayoutData(layoutData);
		scrollable.setFont(JFaceResources.getTextFont());
		if (Util.isWindows()) {
			scrollable.setBackground(scrollable.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		}
		redirectStreams(); // SystemConsole & System.out will print on this SWT Text Widget. 
	}

	protected Scrollable createText(final Composite parent) {
		return new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
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

	public Text getText() {
		return (Text) scrollable;
	}

	public void clear() {
		getText().setText("");
	}

	protected void failSafePrint(final String value) {
		defaultSysOut.print(value);
	}

	protected void doPrint(final String value, final int maxChars) {
		final Text text = getText();
		if (text.getCharCount() < maxChars) {
			text.append(value);
		}
		else {
			text.setText(value.startsWith(newLine) ? value.substring(newLine.length()) : value);
		}
		text.setTopIndex(text.getLineCount() - 1);
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

}
