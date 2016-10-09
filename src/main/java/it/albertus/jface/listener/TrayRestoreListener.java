package it.albertus.jface.listener;

import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TrayItem;

public class TrayRestoreListener extends ShellAdapter implements Listener {

	private final Shell shell;
	private final TrayItem trayItem;

	public TrayRestoreListener(final Shell shell, final TrayItem trayItem) {
		this.shell = shell;
		this.trayItem = trayItem;
	}

	@Override
	public void handleEvent(final Event event) {
		shell.setVisible(true);
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
