package it.albertus.jface.preference.field;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public interface FieldEditorDefault {

	FieldEditorFontFormatter fontFormatter = new FieldEditorFontFormatter();

	boolean isDefaultToolTip();

	boolean isBoldCustomValues();

	void setDefaultToolTip(boolean defaultToolTip);

	void setBoldCustomValues(boolean boldCustomValues);

	class FieldEditorFontFormatter {

		private static final FontRegistry fontRegistry = JFaceResources.getFontRegistry();

		private final String fontKey;

		protected FieldEditorFontFormatter() {
			this.fontKey = this.getClass().getName();
		}

		protected void updateFontStyle(final Text text, final Object defaultValue) {
			updateFontStyle(text, String.valueOf(defaultValue));
		}

		protected void updateFontStyle(final Text text, final String defaultValue) {
			updateFontStyle(text, defaultValue, text.getText());
		}

		protected void updateFontStyle(final Control control, final String defaultValue, final String actualValue) {
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

		protected void setNormalFontStyle(final Control control) {
			if (!fontRegistry.hasValueFor(fontKey)) {
				fontRegistry.put(fontKey, control.getFont().getFontData());
			}
			control.setFont(fontRegistry.get(fontKey));
		}

		protected void setBoldFontStyle(final Control control) {
			if (!fontRegistry.hasValueFor(fontKey)) {
				fontRegistry.put(fontKey, control.getFont().getFontData());
			}
			control.setFont(fontRegistry.getBold(fontKey));
		}
	}

}
