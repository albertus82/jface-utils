package it.albertus.jface.console;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class TextConsoleExample extends AbstractTextConsoleExample<Text> {

	public static void main(final String... args) {
		Display.setAppName(TextConsole.class.getSimpleName() + " Example");
		new TextConsoleExample().run();
	}

	@Override
	protected AbstractTextConsole<Text> createTextConsole(final Composite parent) {
		return new TextConsole(parent, null, true);
	}

}
