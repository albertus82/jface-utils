package it.albertus.jface.preference;

import java.io.IOException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import it.albertus.jface.EnhancedErrorDialog;
import it.albertus.util.PropertiesConfiguration;

public class PreferencesExample {

	private static PropertiesConfiguration configuration = null;

	public static void main(final String... args) throws IOException {
		Display.setAppName("Preferences Example");
		new PreferencesExample().run();
	}

	private void run() {
		final Display display = Display.getDefault();

		try {
			configuration = new PropertiesConfiguration("configuration.properties");
		}
		catch (final IOException ioe) {
			EnhancedErrorDialog.openError(null, "Error", "Cannot open configuration file.", IStatus.ERROR, ioe, new Image[] { display.getSystemImage(SWT.ICON_ERROR) });
		}

		if (configuration != null) {
			final Preferences preferences = new Preferences(MyPageDefinition.values(), MyPreference.values(), configuration, new Image[] { display.getSystemImage(SWT.ICON_QUESTION) });
			preferences.setDialogTitle("Preferences");

			final Shell shell = new Shell(display);
			shell.setText("Preferences example");
			shell.setImage(display.getSystemImage(SWT.ICON_INFORMATION));
			shell.setLayout(new FillLayout());

			final Button button = new Button(shell, SWT.NONE);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent se) {
					System.out.println("Before: " + configuration.getProperties());
					try {
						preferences.openDialog(shell);
					}
					catch (final IOException ioe) {
						EnhancedErrorDialog.openError(shell, "Error", "Cannot open configuration file.", IStatus.ERROR, ioe, new Image[] { display.getSystemImage(SWT.ICON_ERROR) });
					}
					System.out.println("After:  " + configuration.getProperties());
				}
			});
			button.setText("Open preferences dialog");

			shell.pack();
			shell.setSize(shell.getSize().x * 2, (short) (shell.getSize().y * 1.5));
			shell.open();

			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		}
		display.dispose();
	}

}
