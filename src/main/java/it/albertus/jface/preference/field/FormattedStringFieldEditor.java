package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceResources;
import it.albertus.jface.TextFormatter;

import java.util.prefs.Preferences;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class FormattedStringFieldEditor extends StringFieldEditor {

	public FormattedStringFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		init();
	}

	public FormattedStringFieldEditor(final String name, final String labelText, final int width, final Composite parent) {
		super(name, labelText, width, parent);
		init();
	}

	public FormattedStringFieldEditor(final String name, final String labelText, final int width, final int strategy, final Composite parent) {
		super(name, labelText, width, strategy, parent);
		init();
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
			getTextControl().setToolTipText(JFaceResources.get("lbl.preferences.default.value", defaultValue));
		}
	}

	protected void updateFontStyle() {
		final String defaultValue = getPreferenceStore().getDefaultString(getPreferenceName());
		if (defaultValue != null && !defaultValue.isEmpty()) {
			TextFormatter.updateFontStyle(getTextControl(), defaultValue);
		}
	}

	protected void init() {
		setErrorMessage(JFaceResources.get("err.preferences.string"));
		setTextLimit(Preferences.MAX_VALUE_LENGTH);
	}

}
