package it.albertus.jface.console;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;

public class ListConsoleExample extends ScrollableConsoleExample<List> {

	private static int limit = 10000;

	public static void main(final String... args) {
		if (args.length > 0) {
			limit = Integer.parseInt(args[0]);
		}
		Display.setAppName(TextConsole.class.getSimpleName() + " Example");
		new ListConsoleExample().run();
	}

	@Override
	protected ScrollableConsole<List> createScrollableConsole(final Composite parent) {
		final ListConsole lc = new ListConsole(parent, null, true);
		lc.setLimit(limit);
		return lc;
	}

}
