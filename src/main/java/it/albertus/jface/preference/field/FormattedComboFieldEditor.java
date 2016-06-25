package it.albertus.jface.preference.field;

import it.albertus.jface.Resources;
import it.albertus.jface.TextFormatter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class FormattedComboFieldEditor extends ComboFieldEditor {

	public FormattedComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	@Override
	protected Combo getComboBoxControl(final Composite parent) {
		Combo combo = getComboBoxControl();
		if (combo == null) {
			combo = new Combo(parent, SWT.READ_ONLY);
			setComboBoxControl(combo);
			combo.setFont(parent.getFont());
			final String[][] entryNamesAndValues = getEntryNamesAndValues();
			for (int i = 0; i < entryNamesAndValues.length; i++) {
				combo.add(entryNamesAndValues[i][0], i);
			}

			combo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent evt) {
					updateValue();
				}
			});
		}
		return combo;
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		setToolTipText(getNameForValue(getDefaultValue()));
		updateFontStyle();
	}

	@Override
	protected void doLoadDefault() {
		super.doLoadDefault();
		updateFontStyle();
	}

	protected String getDefaultValue() {
		return getPreferenceStore().getDefaultString(getPreferenceName());
	}

	protected void updateValue() {
		final String oldValue = getValue();
		final String name = getComboBoxControl().getText();
		setValue(getValueForName(name));
		setPresentsDefaultValue(false);
		fireValueChanged(VALUE, oldValue, getValue());
		updateFontStyle();
	}

	protected void updateFontStyle() {
		final String defaultValue = getDefaultValue();
		if (defaultValue != null && !defaultValue.isEmpty()) {
			TextFormatter.updateFontStyle(getComboBoxControl(), defaultValue, getValue());
		}
	}

	protected String getNameForValue(final String value) {
		final String[][] entryNamesAndValues = getEntryNamesAndValues();
		for (int i = 0; i < entryNamesAndValues.length; i++) {
			final String[] entry = entryNamesAndValues[i];
			if (value.equals(entry[1])) {
				return entry[0];
			}
		}
		return entryNamesAndValues[0][1];
	}

	protected void setToolTipText(final String defaultValue) {
		if (getComboBoxControl() != null && !getComboBoxControl().isDisposed() && defaultValue != null && !defaultValue.isEmpty()) {
			getComboBoxControl().setToolTipText(Resources.get("lbl.preferences.default.value", defaultValue));
		}
	}

}
