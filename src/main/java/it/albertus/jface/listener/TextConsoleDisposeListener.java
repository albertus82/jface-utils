package it.albertus.jface.listener;

import java.io.PrintStream;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

public class TextConsoleDisposeListener implements DisposeListener {

	private final PrintStream formerSysOut;
	private final PrintStream formerSysErr;

	public TextConsoleDisposeListener(final PrintStream defaultSysOut, final PrintStream defaultSysErr) {
		this.formerSysOut = defaultSysOut;
		this.formerSysErr = defaultSysErr;
	}

	@Override
	public void widgetDisposed(final DisposeEvent de) {
		try {
			System.setOut(formerSysOut);
			System.setErr(formerSysErr);
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
