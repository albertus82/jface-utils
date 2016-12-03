package it.albertus.jface.preference.field;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import it.albertus.jface.Formatter;
import it.albertus.jface.JFaceMessages;

public class ValidatedComboFieldEditor extends EditableComboFieldEditor implements FieldEditorDefault {

	private static final Formatter formatter = new Formatter(ValidatedComboFieldEditor.class);

	private boolean valid = true;
	private String errorMessage;
	private boolean emptyStringAllowed = true;

	private boolean defaultToolTip = true;
	private boolean boldCustomValues = true;

	private ControlDecoration controlDecorator;

	public ValidatedComboFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
		setErrorMessage(JFaceMessages.get("err.preferences.combo.empty"));
		addDecoration();
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
		setToolTipText();
		updateValue();
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

	protected void setToolTipText() {
		if (defaultToolTip) {
			final String defaultValue = getNameForValue(getDefaultValue());
			if (getComboBoxControl() != null && !getComboBoxControl().isDisposed() && defaultValue != null && !defaultValue.isEmpty()) {
				getComboBoxControl().setToolTipText(JFaceMessages.get("lbl.preferences.default.value", defaultValue));
			}
		}
	}

	protected void updateFontStyle() {
		if (boldCustomValues) {
			final String defaultValue = getDefaultValue();
			if (defaultValue != null && !defaultValue.isEmpty()) {
				formatter.updateFontStyle(getComboBoxControl(), defaultValue, getValue());
			}
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

	/**
	 * Determine if the current value is valid or not.
	 * 
	 * <p>
	 * You can invoke {@link #setErrorMessage(String) setErrorMessage}, although
	 * it's not recommended, but <strong>never invoke {@link #showErrorMessage()
	 * showErrorMessage} or {@link #clearErrorMessage() clearErrorMessage}
	 * methods from here</strong>; these methods should be invoked only from
	 * {@link #refreshValidState() refreshValidState}.
	 * </p>
	 * 
	 * @return {@code true} if the field value is valid, and {@code false} if
	 *         invalid.
	 */
	protected boolean checkState() {
		if (getValue() == null || getValue().isEmpty()) {
			if (isEmptyStringAllowed()) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return doCheckState();
		}
	}

	/**
	 * Hook for subclasses to do specific state checks.
	 * 
	 * <p>
	 * You can invoke {@link #setErrorMessage(String) setErrorMessage}, although
	 * it's not recommended, but <strong>never invoke {@link #showErrorMessage()
	 * showErrorMessage} or {@link #clearErrorMessage() clearErrorMessage}
	 * methods from here</strong>; these methods should be invoked only from
	 * {@link #refreshValidState() refreshValidState}.
	 * </p>
	 * 
	 * @return {@code true} if the field value is valid, and {@code false} if
	 *         invalid.
	 */
	protected boolean doCheckState() {
		return true;
	}

	protected void addDecoration() {
		controlDecorator = new ControlDecoration(getComboBoxControl(), SWT.TOP | SWT.LEFT);
		controlDecorator.hide();
		final Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		controlDecorator.setImage(image);
	}

	@Override
	protected void showErrorMessage(final String msg) {
		super.showErrorMessage(msg);
		if (controlDecorator != null) {
			controlDecorator.setDescriptionText(msg);
			controlDecorator.show();
		}
	}

	@Override
	protected void clearErrorMessage() {
		super.clearErrorMessage();
		if (controlDecorator != null && isValid()) {
			controlDecorator.hide();
		}
	}

	protected ControlDecoration getControlDecorator() {
		return controlDecorator;
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
