package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class DefaultRadioGroupFieldEditor extends RadioGroupFieldEditor {

	private final String[][] labelsAndValues;

	private Composite radioBox;

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
		radioBox = super.getRadioBoxControl(parent);
		return radioBox;
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		setToolTipText(getNameForValue(getPreferenceStore().getDefaultString(getPreferenceName())));
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

	protected void setToolTipText(final String defaultValue) {
		if (radioBox != null && !radioBox.isDisposed() && defaultValue != null && !defaultValue.isEmpty()) {
			radioBox.setToolTipText(JFaceMessages.get("lbl.preferences.default.value", defaultValue));
		}
	}

	protected String[][] getLabelsAndValues() {
		return labelsAndValues;
	}

	protected Composite getRadioBox() {
		return radioBox;
	}

}
