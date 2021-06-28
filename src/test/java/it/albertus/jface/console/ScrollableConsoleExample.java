package it.albertus.jface.console;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;

import it.albertus.util.IOUtils;

public abstract class ScrollableConsoleExample<T extends Scrollable> {

	private static volatile int counter = 0;

	private static final Logger logger = Logger.getLogger(ScrollableConsoleExample.class.getName());

	private static final char[] specialChars = { '\u20AC', '\u00E0', '\u00E1', '\u00E8', '\u00E9', '\u00EC', '\u00ED', '\u00F2', '\u00F3', '\u00F9', '\u00FA' };

	// @formatter:off
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
	// @formatter:on

	protected void run() {
		final Display display = Display.getDefault();

		final Shell shell = new Shell(display);
		shell.setImage(display.getSystemImage(SWT.ICON_INFORMATION));
		shell.setLayout(new FillLayout());

		final ScrollableConsole<T> textConsole = createScrollableConsole(shell);

		shell.setText(textConsole.getClass().getSimpleName() + " Example");
		shell.pack();
		shell.setSize(shell.getSize().x * 4, (short) (shell.getSize().y * 3));
		shell.open();

		final Thread printerThread = new Thread() {
			@Override
			public void run() {
				try {
					System.out.print("abcdef");
					TimeUnit.MILLISECONDS.sleep(750);
					System.out.print("ghijklm");
					TimeUnit.MILLISECONDS.sleep(750);
					System.out.print("nopqrs");
					TimeUnit.MILLISECONDS.sleep(750);
					System.out.print("tuvwxyz");
					TimeUnit.MILLISECONDS.sleep(1250);
					for (char c = 'a'; c <= 'z'; c++) {
						System.out.println(c);
					}
					TimeUnit.MILLISECONDS.sleep(1250);
					logger.log(Level.WARNING, String.valueOf(specialChars));
					System.out.println(String.valueOf(specialChars));
					System.err.println(specialChars);
					TimeUnit.MILLISECONDS.sleep(1500);
					while (!Thread.interrupted()) {
						for (final String dummy : loremIpsum) {
							final String text = ++counter + ". " + dummy;
							System.out.println(text);
							logger.log(Level.INFO, text);
							if (counter % 5 == 0) {
								for (int i = 0; i < counter / 5; i++) {
									System.out.println();
								}
							}
							TimeUnit.MILLISECONDS.sleep(333);
						}
					}
				}
				catch (final InterruptedException ie) {
					interrupt();
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
		IOUtils.closeQuietly(textConsole);

		System.out.println("Exit.");

		display.dispose();
	}

	protected abstract ScrollableConsole<T> createScrollableConsole(Composite parent);

}
