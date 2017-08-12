package it.albertus.jface.listener;

import javax.annotation.Nullable;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TrayItem;

/**
 * Listener that restores the shell and optionally hides the tray icon. You
 * should add this listener to both {@link org.eclipse.swt.widgets.Shell Shell}
 * and {@link org.eclipse.swt.widgets.TrayItem TrayItem} widgets.
 */
public class TrayRestoreListener implements SelectionListener, ShellListener {

	private final Shell shell;

	@Nullable
	private final TrayItem trayItem;

	/** Create a listener that restores the shell and hides the tray icon. */
	public TrayRestoreListener(final Shell shell, @Nullable final TrayItem trayItem) {
		this.shell = shell;
		this.trayItem = trayItem;
	}

	/**
	 * Create a listener that restores the shell without hiding the tray icon.
	 */
	public TrayRestoreListener(final Shell shell) {
		this(shell, null);
	}

	@Override
	public void shellDeiconified(@Nullable final ShellEvent se) {
		hideTrayIcon();
	}

	@Override
	public void widgetSelected(@Nullable final SelectionEvent e) {
		if (!shell.isDisposed()) {
			shell.setVisible(true);
		}
		hideTrayIcon();
	}

	@Override
	public void shellActivated(@Nullable final ShellEvent e) {/* Ignore */}

	@Override
	public void shellClosed(@Nullable final ShellEvent e) {/* Ignore */}

	@Override
	public void shellDeactivated(@Nullable final ShellEvent e) {/* Ignore */}

	@Override
	public void shellIconified(@Nullable final ShellEvent e) {/* Ignore */}

	@Override
	public void widgetDefaultSelected(@Nullable final SelectionEvent e) {/* Ignore */}

	protected void hideTrayIcon() {
		if (trayItem != null && !trayItem.isDisposed()) {
			trayItem.setVisible(false);
		}
	}

	protected Shell getShell() {
		return shell;
	}

	@Nullable
	protected TrayItem getTrayItem() {
		return trayItem;
	}

}
