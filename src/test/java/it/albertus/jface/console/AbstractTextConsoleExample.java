package it.albertus.jface.console;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;

import it.albertus.util.ThreadUtils;

public abstract class AbstractTextConsoleExample<T extends Scrollable> {

	private static volatile int counter = 0;

	private static final Logger logger = Logger.getLogger(AbstractTextConsoleExample.class.getName());

	private static final String[] loremIpsum = {
		"Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
		"Donec malesuada eros aliquam, viverra est et, tincidunt nisl.",
		"Mauris finibus ipsum quam, at pretium ipsum semper non.",
		"Maecenas vehicula egestas massa quis mattis.",
		"Phasellus et quam in risus euismod molestie.",
		"Duis consequat mollis pellentesque.",
		"Pellentesque eu leo ut urna aliquet tincidunt.",
		"Suspendisse potenti.",
		"Donec eget est ultricies, cursus turpis a, porttitor ante.",
		"Sed non nibh neque.",
		"Maecenas felis dolor, laoreet rhoncus libero eu, sollicitudin interdum mi.",
		"Duis feugiat mattis eleifend.",
		"Morbi pretium libero nisl, quis volutpat enim mollis nec.",
		"Mauris laoreet erat vel lacinia venenatis.",
		"Integer id urna maximus felis auctor consequat in et justo.",
		"Etiam ullamcorper faucibus nulla dictum fermentum."
	};

	protected void run() {
		final Display display = Display.getDefault();

		final Shell shell = new Shell(display);
		shell.setImage(display.getSystemImage(SWT.ICON_INFORMATION));
		shell.setLayout(new FillLayout());

		final AbstractTextConsole<T> textConsole = createTextConsole(shell);

		shell.setText(textConsole.getClass().getSimpleName() + " Example");
		shell.pack();
		shell.setSize(shell.getSize().x * 4, (short) (shell.getSize().y * 3));
		shell.open();

		final Thread printerThread = new Thread() {
			private boolean exit = false;

			@Override
			public void interrupt() {
				exit = true;
				super.interrupt();
			};

			@Override
			public void run() {
				while (true) {
					for (final String dummy : loremIpsum) {
						final String text = ++counter + ". " + dummy;
						System.out.println(text);
						logger.log(Level.INFO, text);
						ThreadUtils.sleep(250);
						if (exit) {
							return;
						}
					}
				}
			}
		};
		printerThread.start();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		printerThread.interrupt();
		textConsole.close();

		System.out.println("Exit.");

		display.dispose();
	}

	protected abstract AbstractTextConsole<T> createTextConsole(Composite parent);

}
