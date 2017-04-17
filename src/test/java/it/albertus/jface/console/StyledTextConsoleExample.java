package it.albertus.jface.console;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class StyledTextConsoleExample extends ScrollableConsoleExample<StyledText> {

	private static int limit = 10000;

	public static void main(final String... args) {
		if (args.length > 0) {
			limit = Integer.parseInt(args[0]);
		}
		Display.setAppName(StyledTextConsole.class.getSimpleName() + " Example");
		new StyledTextConsoleExample().run();
	}

	@Override
	protected ScrollableConsole<StyledText> createScrollableConsole(final Composite parent) {
		final StyledTextConsole stc = new StyledTextConsole(parent, null, true);
		stc.setLimit(limit);
		return stc;
	}

}
