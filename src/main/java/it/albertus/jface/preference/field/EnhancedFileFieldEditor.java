package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.TextFormatter;

import java.io.File;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class EnhancedFileFieldEditor extends FileFieldEditor implements FieldEditorDefault {

	public static final int MAX_PATH = 255;

	private boolean localized; // Do not set any value here!

	private boolean enforceAbsolute;

	private boolean defaultToolTip = true;
	private boolean boldCustomValues = true;

	private ControlDecoration controlDecorator;

	protected EnhancedFileFieldEditor() {}

	public EnhancedFileFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		init();
	}

	public EnhancedFileFieldEditor(final String name, final String labelText, final boolean enforceAbsolute, final Composite parent) {
		super(name, labelText, enforceAbsolute, parent);
		this.enforceAbsolute = enforceAbsolute;
		init();
	}

	@Override
	protected Button getChangeControl(final Composite parent) {
		final Button browseButton = super.getChangeControl(parent);
		if (!localized) {
			browseButton.setText(JFaceMessages.get("lbl.button.browse"));
			localized = true;
		}
		return browseButton;
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		setToolTipText();
		updateFontStyle();
	}

	@Override
	protected void valueChanged() {
		super.valueChanged();
		updateFontStyle();
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
	protected boolean checkState() {
		String path = getTextControl().getText();
		if (path != null) {
			path = path.trim();
		}
		else {
			path = "";
		}

		String msg = null;
		if (path.length() == 0) {
			if (!isEmptyStringAllowed()) {
				msg = JFaceMessages.get("err.preferences.file.existing");
			}
		}
		else {
			final File file = new File(path);
			if (file.isFile()) {
				if (enforceAbsolute && !file.isAbsolute()) {
					msg = JFaceMessages.get("err.preferences.file.absolute.path");
				}
			}
			else {
				msg = JFaceMessages.get("err.preferences.file.existing");
			}
		}

		if (msg != null) {
			setErrorMessage(msg);
			return false;
		}
		else {
			return doCheckState();
		}
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
				TextFormatter.updateFontStyle(getTextControl(), defaultValue);
			}
		}
	}

	protected void init() {
		setErrorMessage(JFaceMessages.get("err.preferences.file.existing"));
		setTextLimit(MAX_PATH);
		addDecoration();
	}

	protected void addDecoration() {
		controlDecorator = new ControlDecoration(getTextControl(), SWT.TOP | SWT.LEFT);
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
