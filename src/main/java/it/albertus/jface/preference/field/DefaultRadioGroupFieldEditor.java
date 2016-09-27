package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class DefaultRadioGroupFieldEditor extends RadioGroupFieldEditor implements FieldEditorDefault {

	private final String[][] labelsAndValues;

	private Composite radioBox;

	private boolean defaultToolTip = true;

	public DefaultRadioGroupFieldEditor(final String name, final String labelText, final int numColumns, final String[][] labelsAndValues, final Composite parent) {
		super(name, labelText, numColumns, labelsAndValues, parent);
		this.labelsAndValues = labelsAndValues;
	}

	public DefaultRadioGroupFieldEditor(final String name, final String labelText, final int numColumns, final String[][] labelsAndValues, final Composite parent, final boolean useGroup) {
		super(name, labelText, numColumns, labelsAndValues, parent, useGroup);
		this.labelsAndValues = labelsAndValues;
	}

	@Override
	public Composite getRadioBoxControl(final Composite parent) {
		return radioBox = super.getRadioBoxControl(parent);
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		setToolTipText();
	}

	protected String getDefaultValue() {
		return getPreferenceStore().getDefaultString(getPreferenceName());
	}

	protected String getNameForValue(final String value) {
		for (int i = 0; i < labelsAndValues.length; i++) {
			final String[] entry = labelsAndValues[i];
			if (value.equals(entry[1])) {
				return entry[0];
			}
		}
		return labelsAndValues[0][1];
	}

	protected void setToolTipText() {
		if (defaultToolTip) {
			final String defaultValue = getNameForValue(getDefaultValue());
			if (radioBox != null && !radioBox.isDisposed() && defaultValue != null && !defaultValue.isEmpty()) {
				radioBox.setToolTipText(JFaceMessages.get("lbl.preferences.default.value", defaultValue));
			}
		}
	}

	protected String[][] getLabelsAndValues() {
		return labelsAndValues;
	}

	protected Composite getRadioBox() {
		return radioBox;
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
