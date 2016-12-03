package it.albertus.jface.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

import it.albertus.util.Configured;

public class StyledTextConsole extends AbstractTextConsole<StyledText> {

	public StyledTextConsole(final Composite parent, final Object layoutData) {
		this(parent, layoutData, null);
	}

	public StyledTextConsole(final Composite parent, final Object layoutData, final Configured<Integer> maxChars) {
		super(parent, layoutData, maxChars);
		scrollable.setMargins(4, 4, 4, 4); // like the Text control
	}

	@Override
	protected StyledText createScrollable(final Composite parent) {
		return new StyledText(parent, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
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
