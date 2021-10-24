package io.github.albertus82.jface.preference.field;

import javax.annotation.Nullable;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * A non-trimming {@link ValidatedComboFieldEditor} suitable for entries
 * containing whitespaces and tabs.
 */
public class DelimiterComboFieldEditor extends ValidatedComboFieldEditor {

	public DelimiterComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	/**
	 * Return empty string if argument is null, otherwise return the argument as-is.
	 */
	@Override
	protected String cleanValue(@Nullable final String value) {
		return value != null ? value : "";
	}

	/** Try to associate combo text with an existing entry. */
	@Override
	protected void cleanComboText() {
		final Combo combo = getComboBoxControl();
		if (combo != null && !combo.isDisposed()) {
			final String oldText = combo.getText();
			final String newText = getNameForValue(oldText);
			if (!newText.equals(oldText)) {
				combo.setText(newText);
			}
		}
	}

	@Override
	protected boolean checkState() {
		if (getValue() == null || getValue().isEmpty()) {
			return isEmptyStringAllowed();
		}
		else {
			return doCheckState();
		}
	}

}
