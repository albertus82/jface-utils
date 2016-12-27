package it.albertus.jface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Display;

public class SwtUtils {

	public static final char KEY_OPEN = 'o';
	public static final char KEY_SAVE = 's';
	public static final char KEY_SELECT_ALL = 'a';
	public static final char KEY_CUT = 'x';
	public static final char KEY_COPY = 'c';
	public static final char KEY_PASTE = 'v';
	public static final char KEY_DELETE = SWT.DEL;

	private SwtUtils() {
		throw new IllegalAccessError("Utility class");
	}

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

}
