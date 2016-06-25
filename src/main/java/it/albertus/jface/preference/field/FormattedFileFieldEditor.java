package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceResources;
import it.albertus.jface.TextFormatter;

import java.io.File;

import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class FormattedFileFieldEditor extends FileFieldEditor {

	public static final int MAX_PATH = 255;

	private boolean localized; // Do not set any value here!

	private boolean enforceAbsolute;

	protected FormattedFileFieldEditor() {}

	public FormattedFileFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		init();
	}

	public FormattedFileFieldEditor(final String name, final String labelText, final boolean enforceAbsolute, final Composite parent) {
		super(name, labelText, enforceAbsolute, parent);
		this.enforceAbsolute = enforceAbsolute;
		init();
	}

	@Override
	protected Button getChangeControl(final Composite parent) {
		final Button browseButton = super.getChangeControl(parent);
		if (!localized) {
			browseButton.setText(JFaceResources.get("lbl.preferences.button.browse"));
			localized = true;
		}
		return browseButton;
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		setToolTipText(getPreferenceStore().getDefaultString(getPreferenceName()));
		updateFontStyle();
	}

	@Override
	protected void valueChanged() {
		super.valueChanged();
		updateFontStyle();
	}

	@Override
	protected boolean checkState() {
		String msg = null;

		String path = getTextControl().getText();
		if (path != null) {
			path = path.trim();
		}
		else {
			path = "";
		}
		if (path.length() == 0) {
			if (!isEmptyStringAllowed()) {
				msg = getErrorMessage();
			}
		}
		else {
			File file = new File(path);
			if (file.isFile()) {
				if (enforceAbsolute && !file.isAbsolute()) {
					msg = JFaceResources.get("err.preferences.file.absolute.path");
				}
			}
			else {
				msg = getErrorMessage();
			}
		}

		if (msg != null) {
			showErrorMessage(msg);
			return false;
		}

		if (doCheckState()) {
			clearErrorMessage();
			return true;
		}
		msg = getErrorMessage();
		if (msg != null) {
			showErrorMessage(msg);
		}
		return false;
	}

	protected void setToolTipText(final String defaultValue) {
		if (getTextControl() != null && !getTextControl().isDisposed() && defaultValue != null && !defaultValue.isEmpty()) {
			getTextControl().setToolTipText(JFaceResources.get("lbl.preferences.default.value", defaultValue));
		}
	}

	protected void updateFontStyle() {
		final String defaultValue = getPreferenceStore().getDefaultString(getPreferenceName());
		if (defaultValue != null && !defaultValue.isEmpty()) {
			TextFormatter.updateFontStyle(getTextControl(), defaultValue);
		}
	}

	protected void init() {
		setErrorMessage(JFaceResources.get("err.preferences.file.existing"));
		setTextLimit(MAX_PATH);
	}

}
