package it.albertus.jface;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class FontFormatter {

	private static final char SAMPLE_CHAR = '9';

	private static final FontRegistry fontRegistry = JFaceResources.getFontRegistry();

	private final String fontKey;

	public FontFormatter(final String fontKey) {
		this.fontKey = fontKey;
	}

	public void updateFontStyle(final Text text, final Object defaultValue) {
		updateFontStyle(text, String.valueOf(defaultValue));
	}

	public void updateFontStyle(final Text text, final String defaultValue) {
		updateFontStyle(text, defaultValue, text.getText());
	}

	public void updateFontStyle(final Control control, final String defaultValue, final String actualValue) {
		if (checkControl(control)) {
			if (!defaultValue.equals(actualValue)) {
				setBoldFontStyle(control);
			}
			else {
				setNormalFontStyle(control);
			}
		}
	}

	private boolean checkControl(final Control control) {
		return control != null && !control.isDisposed() && control.getFont() != null && !control.getFont().isDisposed() && control.getFont().getFontData() != null && control.getFont().getFontData().length != 0;
	}

	public void setNormalFontStyle(final Control control) {
		if (!fontRegistry.hasValueFor(fontKey)) {
			fontRegistry.put(fontKey, control.getFont().getFontData());
		}
		control.setFont(fontRegistry.get(fontKey));
	}

	public void setBoldFontStyle(final Control control) {
		if (!fontRegistry.hasValueFor(fontKey)) {
			fontRegistry.put(fontKey, control.getFont().getFontData());
		}
		control.setFont(fontRegistry.getBold(fontKey));
	}

	public int computeWidth(final Control control, final int size, final int weight) {
		return computeWidth(control, size, weight, Character.toString(SAMPLE_CHAR));
	}

	public int computeWidth(final Control control, final int size, final int weight, final char character) {
		return computeWidth(control, size, weight, Character.toString(character));
	}

	public int computeWidth(final Control control, final int weight, final String string) {
		return computeWidth(control, 1, weight, string);
	}

	private int computeWidth(final Control control, final int multiplier, final int weight, final String string) {
		int widthHint = SWT.DEFAULT;
		if (control != null && !control.isDisposed()) {
			final Font font = control.getFont(); // Backup initial font.
			if (weight == SWT.BOLD) {
				setBoldFontStyle(control);
			}
			else {
				setNormalFontStyle(control);
			}
			final GC gc = new GC(control);
			try {
				final Point extent = gc.textExtent(string);
				widthHint = (int) (multiplier * extent.x * 1.1);
			}
			finally {
				gc.dispose();
			}
			control.setFont(font); // Restore initial font.
		}
		return widthHint;
	}

}
