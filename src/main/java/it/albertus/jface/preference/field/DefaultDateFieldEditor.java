package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.TextFormatter;

import org.eclipse.swt.widgets.Composite;

public class DefaultDateFieldEditor extends DateFieldEditor {

	public DefaultDateFieldEditor(final String name, final String labelText, final String pattern, final Composite parent) {
		super(name, labelText, pattern, parent);
	}

	public DefaultDateFieldEditor(final String name, final String labelText, final String pattern, final int width, final Composite parent) {
		super(name, labelText, pattern, width, parent);
	}

	public DefaultDateFieldEditor(final String name, final String labelText, final String pattern, final int width, final int strategy, final Composite parent) {
		super(name, labelText, pattern, width, strategy, parent);
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		setToolTipText(getPreferenceStore().getDefaultString(getPreferenceName()));
		updateFontStyle();
	}

	@Override
	protected void valueChanged() {
		super.valueChanged();
		updateFontStyle();
	}

	protected void setToolTipText(final String defaultValue) {
		if (getTextControl() != null && !getTextControl().isDisposed() && defaultValue != null && !defaultValue.isEmpty()) {
			getTextControl().setToolTipText(JFaceMessages.get("lbl.preferences.default.value", defaultValue));
		}
	}

	protected void updateFontStyle() {
		final String defaultValue = getPreferenceStore().getDefaultString(getPreferenceName());
		if (defaultValue != null && !defaultValue.isEmpty()) {
			TextFormatter.updateFontStyle(getTextControl(), defaultValue);
		}
	}

}
