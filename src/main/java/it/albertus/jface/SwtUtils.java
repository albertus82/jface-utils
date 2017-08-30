package it.albertus.jface;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import it.albertus.util.logging.LoggerFactory;

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
	private static final String SWT_GTK3 = "SWT_GTK3";

	private static final Logger logger = LoggerFactory.getLogger(SwtUtils.class);

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

	/**
	 * Check if SWT is running on GTK3 or not.
	 * 
	 * @return <tt>true</tt> if SWT is running on GTK3, <tt>false</tt> if SWT
	 *         isn't running on GTK3 (e.g., GTK2, Windows, macOS), or <b>
	 *         <tt>null</tt> if the GTK version is not determinable.</b>
	 */
	@Nullable
	public static Boolean isGtk3() {
		try {
			return isGtk3(Util.isGtk(), SWT.getVersion(), System.getProperty(ORG_ECLIPSE_SWT_INTERNAL_GTK_VERSION), System.getProperty(SWT_GTK3) != null ? System.getProperty(SWT_GTK3) : System.getenv(SWT_GTK3));
		}
		catch (final SecurityException e) {
			logger.log(Level.WARNING, e.toString(), e);
			return null;
		}
	}

	// Q: Which GTK version do I need to run SWT?
	// A: SWT requires the following GTK+ versions (or newer) to be installed:
	// 
	//   * Eclipse/SWT 4.5.x and newer: GTK+ 2.18.0 and its dependencies (for GTK+ 2) OR GTK+ 3.0.0 and its dependencies (for GTK+ 3)
	//   * Eclipse/SWT 4.4.x: GTK+ 2.10.0 and its dependencies (for GTK+ 2) OR GTK+ 3.0.0 and its dependencies (for GTK+ 3)
	//   * Eclipse/SWT 4.3.x: GTK+ 2.10.0 and its dependencies
	//   * Eclipse/SWT 3.8.x: GTK+ 2.6.0 and its dependencies
	//   * Eclipse/SWT 3.6.x - 3.7.x: GTK+ 2.4.1 and its dependencies
	//   * Eclipse/SWT 3.0.x - 3.5.x: GTK+ 2.2.1 and its dependencies
	//   * Eclipse/SWT 2.1.x: GTK+ 2.0.6 and its dependencies
	// 
	// Note that Eclipse/SWT 4.3.x includes early access support for GTK+ 3.x. To use it on a Linux distro with GTK+ 3.x libraries installed, set Linux environment variable SWT_GTK3=1 before launching your application.
	// Starting from Eclipse/SWT 4.4.x, Linux builds come with GTK+ 3 support enabled by default. You can force Eclipse/SWT to use GTK+ 2 by setting the environment variable SWT_GTK3 to 0, if needed.
	// 
	// You can determine which version(s) of GTK you have installed with rpm -q gtk2 or rpm -q gtk3.
	// 
	// Q: Which GTK version is being used by SWT?
	// A: Since Mars (4.5), SWT sets the org.eclipse.swt.internal.gtk.version system property to the version being used. To display this value in Eclipse, go to Help > Installation Details > Configuration.
	// Look for the line: org.eclipse.swt.internal.gtk.version=3.14.12, where 3.14.12 corresponds to the GTK version currently used by Eclipse.
	@Nullable
	static boolean isGtk3(final boolean isGtk, final int swtVersion, @Nullable final String gtkVersion, @Nullable final String swtGtk3) {
		if (!isGtk) { // Windows, macOS, etc.
			return false;
		}
		if (gtkVersion != null) { // only since SWT 4.5.0; if present, trust this!
			return gtkVersion.startsWith("3");
		}
		else {
			if (swtVersion < 4300) { // < 4.3.0
				return false;
			}
			else if (swtVersion >= 4300 && swtVersion < 4400) { // = 4.3.x
				return swtGtk3 != null && "1".equals(swtGtk3);
			}
			else { // >= 4.4.0
				return swtGtk3 == null || !"0".equals(swtGtk3);
			}
		}
	}

}
