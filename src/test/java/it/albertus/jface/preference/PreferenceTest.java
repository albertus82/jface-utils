package it.albertus.jface.preference;

import it.albertus.util.IConfiguration;
import it.albertus.util.PropertiesConfiguration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class PreferenceTest {

	public static void main(final String... args) {
		new PreferenceTest().run();
	}

	private IConfiguration configuration = new PropertiesConfiguration("configuration.properties");

	private void run() {
		final Display display = Display.getDefault();

		final Shell shell = new Shell(display);
		shell.setText("Preferences");
		shell.setLayout(new FillLayout());

		final Button button = new Button(shell, SWT.NONE);

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Preferences p = new Preferences(configuration, PageDefinition.values(), Preference.values());
				p.openDialog(shell);
			}
		});
		button.setText("Preferences");

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
