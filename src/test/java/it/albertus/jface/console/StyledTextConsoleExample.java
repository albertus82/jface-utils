package it.albertus.jface.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import it.albertus.util.ThreadUtils;

public class StyledTextConsoleExample {

	private static volatile int counter = 0;

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

	public static void main(final String... args) {
		Display.setAppName(StyledTextConsole.class.getSimpleName() + " Example");
		new StyledTextConsoleExample().run();
	}

	private void run() {
		final Display display = Display.getDefault();

		final Shell shell = new Shell(display);
		shell.setText(StyledTextConsole.class.getSimpleName() + " Example");
		shell.setImage(display.getSystemImage(SWT.ICON_INFORMATION));
		shell.setLayout(new FillLayout());

		final StyledTextConsole styledTextConsole = new StyledTextConsole(shell, null, true);

		shell.pack();
		shell.setSize(shell.getSize().x * 4, (short) (shell.getSize().y * 3));
		shell.open();

		Thread printerThread = new Thread() {
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
						System.out.println(++counter + ". " + dummy);
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
		styledTextConsole.close();

		System.out.println("Exit.");

		display.dispose();
	}

}
