package it.albertus.jface;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class SwtUtils {

	public static final char KEY_OPEN = 'o';
	public static final char KEY_SAVE = 's';
	public static final char KEY_SELECT_ALL = 'a';
	public static final char KEY_CUT = 'x';
	public static final char KEY_COPY = 'c';
	public static final char KEY_PASTE = 'v';
	public static final char KEY_DELETE = SWT.DEL;
	public static final char KEY_UNDO = 'z';
	public static final char KEY_REDO = 'y';

	private static final String ORG_ECLIPSE_SWT_INTERNAL_GTK_VERSION = "org.eclipse.swt.internal.gtk.version";

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

	public static int convertHorizontalDLUsToPixels(final Control control, final int dlus) {
		final GC gc = new GC(control);
		gc.setFont(control.getFont());
		final int widthInPixel = Dialog.convertHorizontalDLUsToPixels(gc.getFontMetrics(), dlus);
		gc.dispose();
		return widthInPixel;
	}

	public static boolean isGtk3() {
		final String gtkVersion = System.getProperty(ORG_ECLIPSE_SWT_INTERNAL_GTK_VERSION);
		if (gtkVersion == null) {
			return Util.isGtk();
		}
		else {
			return Util.isGtk() && System.getProperty(ORG_ECLIPSE_SWT_INTERNAL_GTK_VERSION).startsWith("3");
		}
	}

}
