package it.albertus.jface.listener;

import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TrayItem;

/**
 * Listener that restores the shell and optionally hides the tray icon. You can
 * add this listener to both {@link org.eclipse.swt.widgets.Shell Shell} and
 * {@link org.eclipse.swt.widgets.TrayItem TrayItem}.
 */
public class TrayRestoreListener extends ShellAdapter implements Listener {

	private final Shell shell;
	private final TrayItem trayItem;

	/** Create a listener that restores shell and hides tray icon. */
	public TrayRestoreListener(final Shell shell, final TrayItem trayItem) {
		this.shell = shell;
		this.trayItem = trayItem;
	}

	/** Create a listener that restores shell without hiding tray icon. */
	public TrayRestoreListener(final Shell shell) {
		this(shell, null);
	}

	@Override
	public void handleEvent(final Event event) {
		if (!shell.isDisposed()) {
			shell.setVisible(true);
		}
		hideTrayIcon();
	}

	@Override
	public void shellDeiconified(final ShellEvent se) {
		hideTrayIcon();
	}

	protected void hideTrayIcon() {
		if (trayItem != null && !trayItem.isDisposed()) {
			trayItem.setVisible(false);
		}
	}

}
