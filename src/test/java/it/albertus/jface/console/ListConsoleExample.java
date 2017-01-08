package it.albertus.jface.console;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;

public class ListConsoleExample extends ScrollableConsoleExample<List> {

	public static void main(final String... args) {
		Display.setAppName(TextConsole.class.getSimpleName() + " Example");
		new ListConsoleExample().run();
	}

	@Override
	protected ScrollableConsole<List> createScrollableConsole(final Composite parent) {
		return new ListConsole(parent, null, true);
	}

}
