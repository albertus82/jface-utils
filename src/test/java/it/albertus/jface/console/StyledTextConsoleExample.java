package it.albertus.jface.console;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class StyledTextConsoleExample extends ScrollableConsoleExample<StyledText> {

	public static void main(final String... args) {
		Display.setAppName(StyledTextConsole.class.getSimpleName() + " Example");
		new StyledTextConsoleExample().run();
	}

	@Override
	protected ScrollableConsole<StyledText> createScrollableConsole(final Composite parent) {
		return new StyledTextConsole(parent, null, true);
	}

}
