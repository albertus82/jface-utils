package it.albertus.jface.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

public class ListConsole extends ScrollableConsole<List> {

	public static final int DEFAULT_LIMIT = 10000;

	private final StringBuilder charBuffer = new StringBuilder();

	protected ListConsole(final Composite parent, final Object layoutData, final boolean redirectSystemStream) {
		super(parent, layoutData, redirectSystemStream);
	}

	@Override
	public void clear() {
		scrollable.removeAll();
	}

	@Override
	public boolean isEmpty() {
		return scrollable.getItemCount() == 0;
	}

	@Override
	public boolean hasSelection() {
		return scrollable.getSelectionCount() > 0;
	}

	public int getLastItemIndex() {
		return scrollable.getItemCount() - 1;
	}

	@Override
	protected List createScrollable(final Composite parent) {
		return new List(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	}

	@Override
	protected int getDefaultLimit() {
		return DEFAULT_LIMIT;
	}

	@Override
	protected void doPrint(final String value, final int maxChars) {
		scrollable.setRedraw(false);

		if (getLastItemIndex() > getLimit()) {
			clear();
		}

		charBuffer.setLength(0);
		if (isEmpty()) {
			scrollable.add("");
		}
		else {
			charBuffer.append(scrollable.getItem(getLastItemIndex()));
		}
		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) == '\r' && value.length() > i + 1 && value.charAt(i + 1) == '\n') {
				newLine();
				i++;
			}
			else if (value.charAt(i) == '\r' || value.charAt(i) == '\n') {
				newLine();
			}
			else {
				charBuffer.append(value.charAt(i));
			}
		}
		scrollable.setItem(getLastItemIndex(), charBuffer.toString());
		scrollable.setTopIndex(getLastItemIndex());

		scrollable.setRedraw(true);
	}

	private void newLine() {
		scrollable.setItem(Math.max(getLastItemIndex(), 0), charBuffer.toString());
		charBuffer.setLength(0);
		scrollable.add("");
	}

}
