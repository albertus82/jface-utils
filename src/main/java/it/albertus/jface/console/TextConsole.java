package it.albertus.jface.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class TextConsole extends AbstractTextConsole<Text> {

	public TextConsole(final Composite parent, final Object layoutData, final boolean redirectSystemStreams) {
		super(parent, layoutData, redirectSystemStreams);
	}

	@Override
	protected Text createScrollable(final Composite parent) {
		return new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
	}

	@Override
	public void clear() {
		scrollable.setText("");
	}

	@Override
	protected void doPrint(final String value, final int maxChars) {
		if (scrollable.getCharCount() < maxChars) {
			scrollable.append(value);
		}
		else {
			scrollable.setText(value.startsWith(newLine) ? value.substring(newLine.length()) : value);
		}
		scrollable.setTopIndex(scrollable.getLineCount() - 1);
	}

}
