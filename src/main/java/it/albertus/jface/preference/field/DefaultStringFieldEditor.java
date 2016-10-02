package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.TextFormatter;
import it.albertus.jface.preference.decoration.StringFieldEditorDecoration;
import it.albertus.jface.preference.validation.StringFieldEditorValidator;
import it.albertus.jface.validation.TextValidator;

import java.util.prefs.Preferences;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DefaultStringFieldEditor extends StringFieldEditor implements FieldEditorDefault {

	private boolean defaultToolTip = true;
	private boolean boldCustomValues = true;

	protected DefaultStringFieldEditor() {}

	public DefaultStringFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		init();
	}

	public DefaultStringFieldEditor(final String name, final String labelText, final int width, final Composite parent) {
		super(name, labelText, width, parent);
		init();
	}

	public DefaultStringFieldEditor(final String name, final String labelText, final int width, final int strategy, final Composite parent) {
		super(name, labelText, width, strategy, parent);
		init();
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		setToolTipText();
		updateFontStyle();
	}

	@Override
	protected void doLoadDefault() {
		final Text text = getTextControl();
		if (text != null) {
			final String value = getDefaultValue();
			text.setText(value);
		}
		valueChanged();
		getTextControl().notifyListeners(SWT.KeyUp, null); // Refresh error status
	}

	@Override
	protected void valueChanged() {
		super.valueChanged();
		updateFontStyle();
	}

	protected String getDefaultValue() {
		return getPreferenceStore().getDefaultString(getPreferenceName());
	}

	protected void setToolTipText() {
		if (defaultToolTip) {
			final String defaultValue = getDefaultValue();
			if (getTextControl() != null && !getTextControl().isDisposed() && defaultValue != null && !defaultValue.isEmpty()) {
				getTextControl().setToolTipText(JFaceMessages.get("lbl.preferences.default.value", defaultValue));
			}
		}
	}

	protected void updateFontStyle() {
		if (boldCustomValues) {
			final String defaultValue = getDefaultValue();
			if (defaultValue != null && !defaultValue.isEmpty()) {
				TextFormatter.updateFontStyle(getTextControl(), defaultValue);
			}
		}
	}

	protected void init() {
		setErrorMessage(JFaceMessages.get("err.preferences.string"));
		setTextLimit(Preferences.MAX_VALUE_LENGTH);
		addDecoration();
	}

	protected void addDecoration() {
		final TextValidator validator = new StringFieldEditorValidator(getTextControl(), this);
		new StringFieldEditorDecoration(validator, this);
	}

	@Override
	public boolean isDefaultToolTip() {
		return defaultToolTip;
	}

	@Override
	public void setDefaultToolTip(final boolean defaultToolTip) {
		this.defaultToolTip = defaultToolTip;
	}

	@Override
	public boolean isBoldCustomValues() {
		return boldCustomValues;
	}

	@Override
	public void setBoldCustomValues(final boolean boldCustomValues) {
		this.boldCustomValues = boldCustomValues;
	}

}
