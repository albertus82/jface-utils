package it.albertus.jface.preference.field;

import it.albertus.jface.preference.field.ValidatedComboFieldEditor;

import org.eclipse.swt.widgets.Composite;

public class DelimiterComboFieldEditor extends ValidatedComboFieldEditor {

	public DelimiterComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	@Override
	protected String cleanValue(final String value) {
		return value != null ? value : "";
	}

	@Override
	protected void cleanComboText() {
		final String oldText = getComboBoxControl().getText();
		final String newText = getNameForValue(oldText);
		if (!newText.equals(oldText)) {
			getComboBoxControl().setText(newText);
		}
	}

}
