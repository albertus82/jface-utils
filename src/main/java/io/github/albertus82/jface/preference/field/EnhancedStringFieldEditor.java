package io.github.albertus82.jface.preference.field;

import java.util.prefs.Preferences;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import io.github.albertus82.jface.Formatter;
import io.github.albertus82.jface.JFaceMessages;

public class EnhancedStringFieldEditor extends StringFieldEditor implements FieldEditorDefault {

	private static final Formatter formatter = new Formatter(EnhancedStringFieldEditor.class);

	private boolean defaultToolTip = true;
	private boolean boldCustomValues = true;

	private ControlDecoration controlDecorator;

	protected EnhancedStringFieldEditor() {}

	public EnhancedStringFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		init();
	}

	public EnhancedStringFieldEditor(final String name, final String labelText, final int width, final Composite parent) {
		super(name, labelText, width, parent);
		init();
	}

	public EnhancedStringFieldEditor(final String name, final String labelText, final int width, final int strategy, final Composite parent) {
		super(name, labelText, width, strategy, parent);
		init();
	}

	@Override
	protected void doFillIntoGrid(final Composite parent, final int numColumns) {
		super.doFillIntoGrid(parent, numColumns);
		final Control control = getTextControl();
		if (control != null && !control.isDisposed() && control.getLayoutData() instanceof GridData) {
			final GridData gd = (GridData) control.getLayoutData();
			gd.widthHint = control.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
			gd.grabExcessHorizontalSpace = false;
		}
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		setToolTipText();
		updateFontStyle();
	}

	@Override
	protected void doLoadDefault() {
		final Text text = getTextControl();
		if (text != null) {
			final String value = getDefaultValue();
			text.setText(value);
		}
		valueChanged();
	}

	@Override
	protected void valueChanged() {
		super.valueChanged();
		updateFontStyle();
	}

	protected String getDefaultValue() {
		return getPreferenceStore().getDefaultString(getPreferenceName());
	}

	protected void setToolTipText() {
		if (defaultToolTip) {
			final String defaultValue = getDefaultValue();
			if (getTextControl() != null && !getTextControl().isDisposed() && defaultValue != null && !defaultValue.isEmpty()) {
				getTextControl().setToolTipText(JFaceMessages.get("lbl.preferences.default.value", defaultValue));
			}
		}
	}

	protected void updateFontStyle() {
		if (boldCustomValues) {
			final String defaultValue = getDefaultValue();
			if (defaultValue != null && !defaultValue.isEmpty()) {
				formatter.updateFontStyle(getTextControl(), defaultValue);
			}
		}
	}

	protected void init() {
		setErrorMessage(JFaceMessages.get("err.preferences.string"));
		setTextLimit(Preferences.MAX_VALUE_LENGTH);
		addDecoration();
	}

	protected void addDecoration() {
		controlDecorator = new ControlDecoration(getTextControl(), SWT.TOP | SWT.LEFT);
		controlDecorator.hide();
		final Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		controlDecorator.setImage(image);
	}

	/**
	 * Determine if the current value is valid or not.
	 * 
	 * <p>
	 * You can invoke {@link #setErrorMessage(String) setErrorMessage}, although
	 * it's not recommended, but <strong>never invoke {@link #showErrorMessage()
	 * showErrorMessage} or {@link #clearErrorMessage() clearErrorMessage} methods
	 * from here</strong>; these methods should be invoked only from
	 * {@link #refreshValidState() refreshValidState}.
	 * </p>
	 * 
	 * @return {@code true} if the field value is valid, and {@code false} if
	 *         invalid.
	 */
	@Override
	protected boolean checkState() {
		final Text text = getTextControl();
		if (text == null) {
			return false;
		}
		if (text.getText().isEmpty()) {
			return isEmptyStringAllowed();
		}
		return doCheckState();
	}

	/**
	 * Hook for subclasses to do specific state checks.
	 * 
	 * <p>
	 * You can invoke {@link #setErrorMessage(String) setErrorMessage}, although
	 * it's not recommended, but <strong>never invoke {@link #showErrorMessage()
	 * showErrorMessage} or {@link #clearErrorMessage() clearErrorMessage} methods
	 * from here</strong>; these methods should be invoked only from
	 * {@link #refreshValidState() refreshValidState}.
	 * </p>
	 * 
	 * @return {@code true} if the field value is valid, and {@code false} if
	 *         invalid.
	 */
	@Override
	protected boolean doCheckState() { // NOSONAR Javadoc
		return super.doCheckState();
	}

	@Override
	protected void refreshValidState() {
		super.refreshValidState();
		final String errorMessage = getErrorMessage();
		if (errorMessage != null && !errorMessage.isEmpty()) {
			if (isValid()) {
				clearErrorMessage();
			}
			else {
				showErrorMessage();
			}
		}
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

	protected ControlDecoration getControlDecorator() {
		return controlDecorator;
	}

}
