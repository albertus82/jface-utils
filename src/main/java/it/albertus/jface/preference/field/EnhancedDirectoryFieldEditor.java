package it.albertus.jface.preference.field;

import java.io.File;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Text;

import it.albertus.jface.Formatter;
import it.albertus.jface.JFaceMessages;
import it.albertus.util.ISupplier;

public class EnhancedDirectoryFieldEditor extends DirectoryFieldEditor implements FieldEditorDefault {

	private static final Formatter formatter = new Formatter(EnhancedDirectoryFieldEditor.class);

	public static final int MAX_PATH = 255;

	private boolean localized; // Do not set any value here!

	private File filterPath = null;
	private ISupplier<String> dialogMessage;

	private boolean defaultToolTip = true;
	private boolean boldCustomValues = true;
	private boolean checkExistence = true;

	private ControlDecoration controlDecorator;

	public EnhancedDirectoryFieldEditor(final String name, final String labelText, final Composite parent) {
		super(name, labelText, parent);
		setErrorMessage(JFaceMessages.get("err.preferences.directory"));
		setTextLimit(MAX_PATH);
		addDecoration();
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
	protected String changePressed() {
		File f = new File(getTextControl().getText());
		if (!f.exists()) {
			f = null;
		}
		File d = getDirectory(f);
		if (d == null) {
			valueChanged(); // Force validation on Cancel or Close
			return null;
		}
		return d.getAbsolutePath();
	}

	@Override
	public void setFilterPath(final File filterPath) {
		super.setFilterPath(filterPath);
		this.filterPath = filterPath;
	}

	@Override
	protected boolean checkState() {
		boolean result;
		if (isEmptyStringAllowed()) {
			result = true;
		}
		else if (getTextControl() == null) {
			result = false;
		}
		else {
			String txt = getTextControl().getText();
			result = (txt.trim().length() > 0) || isEmptyStringAllowed();
		}
		// call hook for subclasses
		result = result && doCheckState();

		return result;
	}

	@Override
	protected boolean doCheckState() {
		String fileName = getTextControl().getText();
		fileName = fileName.trim();
		if (fileName.isEmpty() && isEmptyStringAllowed()) {
			return true;
		}
		else if (checkExistence) {
			return new File(fileName).isDirectory();
		}
		else {
			return !fileName.isEmpty();
		}
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
	public void setStringValue(final String value) {
		final Text textField = getTextControl();
		if (textField != null) {
			final String cleanedValue = value != null ? value : "";
			oldValue = textField.getText();
			if (!oldValue.equals(cleanedValue)) {
				textField.setText(cleanedValue);
			}
			valueChanged();
		}
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

	public File getFilterPath() {
		return filterPath;
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

	protected File getDirectory(final File startingDirectory) {
		final DirectoryDialog fileDialog = new DirectoryDialog(getShell(), SWT.OPEN | SWT.SHEET);
		if (dialogMessage != null && dialogMessage.get() != null) {
			fileDialog.setMessage(dialogMessage.get());
		}
		if (startingDirectory != null) {
			fileDialog.setFilterPath(startingDirectory.getPath());
		}
		else if (filterPath != null) {
			fileDialog.setFilterPath(filterPath.getPath());
		}
		String dir = fileDialog.open();
		if (dir != null) {
			dir = dir.trim();
			if (dir.length() > 0) {
				return new File(dir);
			}
		}
		return null;
	}

	public ISupplier<String> getDialogMessage() {
		return dialogMessage;
	}

	public void setDialogMessage(final ISupplier<String> dialogMessage) {
		this.dialogMessage = dialogMessage;
	}

	public void setCheckExistence(final boolean checkExistence) {
		this.checkExistence = checkExistence;
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
