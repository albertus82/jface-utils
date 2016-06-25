package it.albertus.jface;

import org.eclipse.swt.SWT;

public abstract class GuiUtils {

	public static final char KEY_OPEN = 'o';
	public static final char KEY_SAVE = 's';
	public static final char KEY_SELECT_ALL = 'a';
	public static final char KEY_CUT = 'x';
	public static final char KEY_COPY = 'c';
	public static final char KEY_PASTE = 'v';
	public static final char KEY_DELETE = SWT.DEL;

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

}
