package it.albertus.jface.sysinfo;

import java.io.IOException;

import javax.annotation.Nullable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SystemInformationExample {

	private static final String APP_NAME = "System Information Example";

	public static void main(final String... args) throws IOException {
		Display.setAppName(APP_NAME);
		new SystemInformationExample().run();
	}

	private void run() {
		final Display display = Display.getDefault();

		final Shell shell = new Shell(display);
		shell.setText("System Information example");
		shell.setImage(display.getSystemImage(SWT.ICON_INFORMATION));
		shell.setLayout(new FillLayout());

		final Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(@Nullable final SelectionEvent se) {
				SystemInformationDialog.open(shell);
			}
		});
		button.setText("Open System Information dialog");

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
