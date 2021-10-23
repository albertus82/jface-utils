package io.github.albertus82.jface.closeable;

import java.io.Closeable;

import org.eclipse.swt.widgets.Widget;

public class CloseableWidget<T extends Widget> implements Closeable {

	private final T widget;

	public CloseableWidget(final T widget) {
		this.widget = widget;
	}

	public T getWidget() {
		return widget;
	}

	@Override
	public void close() {
		if (widget != null) {
			widget.dispose();
		}
	}

}
