package io.github.albertus82.jface.closeable;

import java.io.Closeable;

import org.eclipse.swt.dnd.Clipboard;

public class CloseableClipboard implements Closeable {

	private final Clipboard clipboard;

	public CloseableClipboard(final Clipboard clipboard) {
		this.clipboard = clipboard;
	}

	public Clipboard getClipboard() {
		return clipboard;
	}

	@Override
	public void close() {
		if (clipboard != null) {
			clipboard.dispose();
		}
	}

}
