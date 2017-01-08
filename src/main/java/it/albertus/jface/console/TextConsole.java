package it.albertus.jface.console;

import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class TextConsole extends ScrollableConsole<Text> {

	public TextConsole(final Composite parent, final Object layoutData, final boolean redirectSystemStreams) {
		super(parent, layoutData, redirectSystemStreams);
	}

	@Override
	protected Text createScrollable(final Composite parent) {
		final Text text = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
		if (Util.isWindows()) {
			text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		}
		return text;
	}

	@Override
	public void clear() {
		scrollable.setText("");
	}

	@Override
	public boolean isEmpty() {
		return scrollable == null || scrollable.isDisposed() || scrollable.getCharCount() == 0;
	}

	@Override
	public boolean hasSelection() {
		return scrollable != null && !scrollable.isDisposed() && scrollable.getSelectionCount() > 0;
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
