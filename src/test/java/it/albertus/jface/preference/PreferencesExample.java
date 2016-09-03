package it.albertus.jface.preference;

import it.albertus.util.PropertiesConfiguration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class PreferencesExample {

	public static void main(final String... args) {
		new PreferencesExample().run();
	}

	private final PropertiesConfiguration configuration = new PropertiesConfiguration("configuration.properties");

	private void run() {
		System.out.println("Before: " + configuration.getProperties());

		final Preferences preferences = new Preferences(MyPageDefinition.values(), MyPreference.values(), configuration);
		preferences.setDialogTitle("Preferences");

		final Display display = Display.getDefault();

		final Shell shell = new Shell(display);
		shell.setText("Preferences example");
		shell.setLayout(new FillLayout());

		final Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent se) {
				preferences.openDialog(shell);
			}
		});
		button.setText("Open preferences dialog");

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

		System.out.println("After:  " + configuration.getProperties());
	}

}
