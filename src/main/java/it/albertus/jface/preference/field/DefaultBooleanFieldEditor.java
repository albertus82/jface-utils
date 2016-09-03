package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class DefaultBooleanFieldEditor extends BooleanFieldEditor {

	private Button checkBox;

	protected DefaultBooleanFieldEditor() {}

	public DefaultBooleanFieldEditor(final String name, final String label, final Composite parent) {
		super(name, label, parent);
	}

	public DefaultBooleanFieldEditor(final String name, final String labelText, final int style, final Composite parent) {
		super(name, labelText, style, parent);
	}

	@Override
	protected Button getChangeControl(final Composite parent) {
		checkBox = super.getChangeControl(parent);
		return checkBox;
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		setToolTipText(getPreferenceStore().getDefaultBoolean(getPreferenceName()));
	}

	protected void setToolTipText(final boolean defaultValue) {
		if (checkBox != null && !checkBox.isDisposed()) {
			final String value = JFaceMessages.get(defaultValue ? "lbl.preferences.default.value.true" : "lbl.preferences.default.value.false");
			checkBox.setToolTipText(JFaceMessages.get("lbl.preferences.default.value", value));
		}
	}

	protected Button getChangeControl() {
		return checkBox;
	}

}
