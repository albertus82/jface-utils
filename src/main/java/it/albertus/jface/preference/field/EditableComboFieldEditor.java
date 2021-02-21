package it.albertus.jface.preference.field;

import java.util.prefs.Preferences;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class EditableComboFieldEditor extends ComboFieldEditor {

	private int textLimit = Preferences.MAX_VALUE_LENGTH;

	public EditableComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	@Override
	protected void doFillIntoGrid(final Composite parent, final int numColumns) {
		int comboColumns = 1;
		if (numColumns > 1) {
			comboColumns = numColumns - 1;
		}
		final Label label = getLabelControl(parent);
		GridDataFactory.swtDefaults().applyTo(label);

		final Combo combo = getComboBoxControl(parent);
		GridDataFactory.swtDefaults().span(comboColumns, 1).align(SWT.FILL, SWT.CENTER).hint(combo.computeSize(SWT.DEFAULT, SWT.DEFAULT).x, SWT.DEFAULT).grab(true, false).applyTo(combo);
		combo.setFont(parent.getFont());
	}

	@Override
	protected Combo getComboBoxControl(final Composite parent) {
		Combo combo = getComboBoxControl();
		if (combo == null) {
			combo = new Combo(parent, SWT.NONE);
			setComboBoxControl(combo);
			combo.setFont(parent.getFont());
			final String[][] entryNamesAndValues = getEntryNamesAndValues();
			for (int i = 0; i < entryNamesAndValues.length; i++) {
				combo.add(entryNamesAndValues[i][0], i);
			}
			combo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent se) {
					updateValue();
				}
			});
			combo.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(final FocusEvent fe) {
					updateValue();
				}
			});
			if (textLimit > 0) {
				combo.setTextLimit(textLimit);
			}
		}
		return combo;
	}

	@Override
	protected void updateComboForValue(final String value) {
		setValue(value);
		getComboBoxControl().setText(getNameForValue(value));
	}

	@Override
	protected String getValueForName(final String name) {
		for (final String[] entry : getEntryNamesAndValues()) {
			if (name.equals(entry[0])) {
				return entry[1];
			}
		}
		return name; // Value not present in the array.
	}

	@Override
	public void store() {
		final Combo combo = getComboBoxControl();
		if (combo != null && !combo.isDisposed()) {
			combo.notifyListeners(SWT.FocusOut, null); // Fix for macOS.
		}
		super.store();
	}

	protected String getNameForValue(final String value) {
		for (final String[] entry : getEntryNamesAndValues()) {
			if (value.equals(entry[1])) {
				return entry[0];
			}
		}
		return value; // Name not present in the array.
	}

	protected void updateValue() {
		final String oldValue = getValue();
		final String name = getComboBoxControl().getText();
		setValue(getValueForName(name));
		setPresentsDefaultValue(false);
		fireValueChanged(VALUE, oldValue, getValue());
	}

	public void setTextLimit(final int limit) {
		textLimit = limit;
		if (getComboBoxControl() != null) {
			getComboBoxControl().setTextLimit(limit);
		}
	}

	protected int getMaxLabelLength() {
		int length = 0;
		for (final String[] entry : getEntryNamesAndValues()) {
			if (entry[0].length() > length) {
				length = entry[0].length();
			}
		}
		return length;
	}

}
