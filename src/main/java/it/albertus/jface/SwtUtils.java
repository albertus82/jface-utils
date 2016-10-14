package it.albertus.jface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public abstract class SwtUtils {

	public static final char KEY_OPEN = 'o';
	public static final char KEY_SAVE = 's';
	public static final char KEY_SELECT_ALL = 'a';
	public static final char KEY_CUT = 'x';
	public static final char KEY_COPY = 'c';
	public static final char KEY_PASTE = 'v';
	public static final char KEY_DELETE = SWT.DEL;

	private static final char SAMPLE_CHAR = '9';

	public static String getMod1ShortcutLabel(final char key) {
		if (SWT.MOD1 != SWT.COMMAND) {
			return "\tCtrl+" + Character.toUpperCase(key);
		}
		else {
			return "";
		}
	}

	public static String getMod1ShortcutLabel(final String key) {
		if (SWT.MOD1 != SWT.COMMAND) {
			return "\tCtrl+" + key;
		}
		else {
			return "";
		}
	}

	public static String getShortcutLabel(final String key) {
		if (SWT.MOD1 != SWT.COMMAND) {
			return "\t" + key;
		}
		else {
			return "";
		}
	}

	public static boolean checkClipboard(final Transfer transfer) {
		final Display display = Display.getCurrent();
		if (display != null) {
			final Clipboard clipboard = new Clipboard(display);
			final TransferData[] clipboardAvailableTypes = clipboard.getAvailableTypes();
			clipboard.dispose();
			for (final TransferData clipboardType : clipboardAvailableTypes) {
				if (transfer.isSupportedType(clipboardType)) {
					return true;
				}
			}
		}
		return false;
	}

	public static int computeControlWidth(final Control control, final int size, final int style) {
		return computeControlWidth(control, size, style, Character.toString(SAMPLE_CHAR));
	}

	public static int computeControlWidth(final Control control, final int size, final int style, final char character) {
		return computeControlWidth(control, size, style, Character.toString(character));
	}

	public static int computeControlWidth(final Control control, final int style, final String string) {
		return computeControlWidth(control, 1, style, string);
	}

	private static int computeControlWidth(final Control control, final int multiplier, final int style, final String string) {
		int computedWidth = SWT.DEFAULT;
		if (control != null && !control.isDisposed()) {
			final Font originalFont = control.getFont(); // Backup original font
			final FontData[] originalFontData = originalFont.getFontData();
			final FontData[] newFontData = new FontData[originalFontData.length];
			for (int i = 0; i < originalFontData.length; i++) {
				final FontData base = originalFontData[i];
				newFontData[i] = new FontData(base.getName(), base.getHeight(), base.getStyle() | style);
			}
			final Font styledFont = new Font(control.getDisplay(), newFontData); // Create temporary font
			try {
				control.setFont(styledFont);
				final GC gc = new GC(control);
				try {
					final Point extent = gc.textExtent(string);
					computedWidth = (int) (multiplier * extent.x * 1.1);
				}
				finally {
					gc.dispose();
				}
			}
			finally {
				control.setFont(originalFont); // Restore original font
				styledFont.dispose(); // Dispose temporary font
			}
		}
		return computedWidth;
	}

}
