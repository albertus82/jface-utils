package it.albertus.jface.preference;

import it.albertus.util.PropertiesConfiguration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class PreferencesExample {

	public static void main(final String... args) {
		Display.setAppName("Preferences Example");
		new PreferencesExample().run();
	}

	private final PropertiesConfiguration configuration = new PropertiesConfiguration("configuration.properties");

	private void run() {
		final Display display = Display.getDefault();

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
				preferences.openDialog(shell);
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
		display.dispose();
	}

}
