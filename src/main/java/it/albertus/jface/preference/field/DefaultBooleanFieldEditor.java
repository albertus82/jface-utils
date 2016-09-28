package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class DefaultBooleanFieldEditor extends BooleanFieldEditor implements FieldEditorDefault {

	private Button checkBox;
	private boolean defaultToolTip = true;

	protected DefaultBooleanFieldEditor() {}

	public DefaultBooleanFieldEditor(final String name, final String label, final Composite parent) {
		super(name, label, parent);
	}

	public DefaultBooleanFieldEditor(final String name, final String labelText, final int style, final Composite parent) {
		super(name, labelText, style, parent);
	}

	@Override
	protected Button getChangeControl(final Composite parent) {
		return checkBox = super.getChangeControl(parent);
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		setToolTipText();
	}

	protected boolean getDefaultValue() {
		return getPreferenceStore().getDefaultBoolean(getPreferenceName());
	}

	protected void setToolTipText() {
		if (defaultToolTip) {
			boolean defaultValue = getDefaultValue();
			if (checkBox != null && !checkBox.isDisposed()) {
				final String value = JFaceMessages.get(defaultValue ? "lbl.preferences.default.value.true" : "lbl.preferences.default.value.false");
				checkBox.setToolTipText(JFaceMessages.get("lbl.preferences.default.value", value));
			}
		}
	}

	protected Button getChangeControl() {
		return checkBox;
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
		return false;
	}

	@Override
	public void setBoldCustomValues(final boolean boldCustomValues) {}

}
