package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceResources;
import it.albertus.jface.TextFormatter;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;

public class ValidatedComboFieldEditor extends EditableComboFieldEditor {

	private boolean valid = true;
	private String errorMessage;
	private boolean emptyStringAllowed = true;

	public ValidatedComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
		setErrorMessage(JFaceResources.get("err.preferences.combo.empty"));
		getComboBoxControl().addKeyListener(new ValidateKeyListener());
	}

	@Override
	protected void updateValue() {
		cleanComboText();
		validate();
		updateFontStyle();
	}

	@Override
	protected void refreshValidState() {
		setValid(checkState());
		final String errorMessage = getErrorMessage();
		if (errorMessage != null && !errorMessage.isEmpty()) {
			if (isValid()) {
				clearErrorMessage();
			}
			else {
				showErrorMessage(errorMessage);
			}
		}
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		setToolTipText(getNameForValue(getDefaultValue()));
		updateFontStyle();
		cleanComboText();
	}

	@Override
	protected void doLoadDefault() {
		super.doLoadDefault();
		updateFontStyle();
	}

	@Override
	protected void updateComboForValue(final String value) {
		super.updateComboForValue(cleanValue(value));
	}

	@Override
	public boolean isValid() {
		return valid;
	}

	protected void setValid(final boolean valid) {
		this.valid = valid;
	}

	protected String getDefaultValue() {
		return getPreferenceStore().getDefaultString(getPreferenceName());
	}

	protected void setToolTipText(final String defaultValue) {
		if (getComboBoxControl() != null && !getComboBoxControl().isDisposed() && defaultValue != null && !defaultValue.isEmpty()) {
			getComboBoxControl().setToolTipText(JFaceResources.get("lbl.preferences.default.value", defaultValue));
		}
	}

	protected void updateFontStyle() {
		final String defaultValue = getDefaultValue();
		if (defaultValue != null && !defaultValue.isEmpty()) {
			TextFormatter.updateFontStyle(getComboBoxControl(), defaultValue, getValue());
		}
	}

	protected void validate() {
		super.updateValue();
		boolean oldValue = valid;
		refreshValidState();
		fireValueChanged(IS_VALID, oldValue, valid);
	}

	/** Trims value (from configuration file) to empty. */
	protected String cleanValue(final String value) {
		return value != null ? value.trim() : "";
	}

	/** Trims combo text and tries to associate it with an existing entry. */
	protected void cleanComboText() {
		final String oldText = getComboBoxControl().getText();
		final String newText = getNameForValue(oldText.trim());
		if (!newText.equals(oldText)) {
			getComboBoxControl().setText(newText);
		}
	}

	protected boolean checkState() {
		if (!emptyStringAllowed && (getValue() == null || getValue().isEmpty())) {
			return false;
		}
		else {
			return true;
		}
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(final String message) {
		this.errorMessage = message;
	}

	public boolean isEmptyStringAllowed() {
		return emptyStringAllowed;
	}

	public void setEmptyStringAllowed(boolean emptyStringAllowed) {
		this.emptyStringAllowed = emptyStringAllowed;
	}

	protected class ValidateKeyListener extends KeyAdapter {
		@Override
		public void keyReleased(final KeyEvent ke) {
			validate();
		}
	}

}
