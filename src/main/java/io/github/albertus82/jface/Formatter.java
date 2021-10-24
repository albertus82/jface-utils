package io.github.albertus82.jface;

import java.util.Arrays;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class Formatter {

	private static final char SAMPLE_CHAR = '9';

	private final String symbolicName;

	public Formatter(final String symbolicName) {
		this.symbolicName = symbolicName;
	}

	public Formatter(final Class<?> clazz) {
		this.symbolicName = clazz.getName();
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
		registerFont(control);
		final Font normal = JFaceResources.getFontRegistry().get(symbolicName);
		if (!Arrays.equals(normal.getFontData(), control.getFont().getFontData())) {
			control.setFont(normal);
			fixTextControlHeight(control);
		}
	}

	public void setBoldFontStyle(final Control control) {
		registerFont(control);
		final Font bold = JFaceResources.getFontRegistry().getBold(symbolicName);
		if (!Arrays.equals(bold.getFontData(), control.getFont().getFontData())) {
			control.setFont(bold);
			fixTextControlHeight(control);
		}
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
		if (control != null && !control.isDisposed()) {
			final Font font = control.getFont(); // Backup initial font.
			GC gc = null;
			try {
				if (weight == SWT.BOLD) {
					setBoldFontStyle(control);
				}
				else {
					setNormalFontStyle(control);
				}
				gc = new GC(control);
				gc.setFont(control.getFont());
				final Point extent = gc.textExtent(string);
				return (int) (multiplier * extent.x * 1.1);
			}
			finally {
				if (gc != null) {
					gc.dispose();
				}
				control.setFont(font); // Restore initial font.
			}
		}
		return SWT.DEFAULT;
	}

	private void registerFont(final Control control) {
		final FontRegistry fontRegistry = JFaceResources.getFontRegistry();
		if (!fontRegistry.hasValueFor(symbolicName)) {
			fontRegistry.put(symbolicName, control.getFont().getFontData());
		}
	}

	private void fixTextControlHeight(final Control control) {
		if (Util.isCocoa() && control instanceof Text) {
			control.getParent().layout(new Control[] { control });
		}
	}

}
