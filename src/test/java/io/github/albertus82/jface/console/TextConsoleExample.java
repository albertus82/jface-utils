package io.github.albertus82.jface.console;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class TextConsoleExample extends ScrollableConsoleExample<Text> {

	private static int limit = 10000;

	public static void main(final String... args) {
		if (args.length > 0) {
			limit = Integer.parseInt(args[0]);
		}
		Display.setAppName(TextConsole.class.getSimpleName() + " Example");
		new TextConsoleExample().run();
	}

	@Override
	protected ScrollableConsole<Text> createScrollableConsole(final Composite parent) {
		final TextConsole tc = new TextConsole(parent, null, true);
		tc.setLimit(limit);
		return tc;
	}

}
