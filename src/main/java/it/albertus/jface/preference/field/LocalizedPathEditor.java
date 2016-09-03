package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;
import it.albertus.util.Localized;

import java.io.File;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;

public class LocalizedPathEditor extends PathEditor {

	private boolean localized; // Do not set any value here!

	private final boolean container;

	private String lastPath;

	private Localized dirChooserLabelText;

	protected LocalizedPathEditor() {
		super();
		container = false;
	}

	public LocalizedPathEditor(final String name, final String labelText, final Localized dirChooserLabelText, final Composite parent) {
		this(name, labelText, dirChooserLabelText, parent, null);
	}

	public LocalizedPathEditor(final String name, final String labelText, final Localized dirChooserLabelText, final Composite parent, final Integer horizontalSpan) {
		super(name, labelText, dirChooserLabelText != null ? dirChooserLabelText.getString() : null, (horizontalSpan != null && horizontalSpan > 0) ? createContainer(parent, horizontalSpan) : parent);
		this.dirChooserLabelText = dirChooserLabelText;
		if (horizontalSpan != null && horizontalSpan > 0) {
			container = true;
		}
		else {
			container = false;
		}
	}

	protected static Composite createContainer(final Composite fieldEditorParent, final int horizontalSpan) {
		final Composite parent = new Composite(fieldEditorParent, SWT.NULL);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).span(horizontalSpan, 1).applyTo(parent);
		return parent;
	}

	@Override
	protected void checkParent(final Control control, final Composite parent) {
		super.checkParent(container ? control.getParent() : control, parent);
	}

	@Override
	public Composite getButtonBoxControl(final Composite parent) {
		final Composite buttonBox = super.getButtonBoxControl(parent);
		if (!localized) {
			final Button addButton = getAddButton();
			addButton.setText(JFaceMessages.get("lbl.preferences.list.button.add"));

			final Button removeButton = getRemoveButton();
			removeButton.setText(JFaceMessages.get("lbl.preferences.list.button.remove"));

			final Button upButton = getUpButton();
			upButton.setText(JFaceMessages.get("lbl.preferences.list.button.up"));

			final Button downButton = getDownButton();
			downButton.setText(JFaceMessages.get("lbl.preferences.list.button.down"));

			localized = true;
		}
		return buttonBox;
	}

	@Override
	protected String getNewInputObject() {
		final DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.SHEET);
		if (dirChooserLabelText != null && dirChooserLabelText.getString() != null) {
			dialog.setMessage(dirChooserLabelText.getString());
		}
		if (lastPath != null) {
			if (new File(lastPath).exists()) {
				dialog.setFilterPath(lastPath);
			}
		}
		String dir = dialog.open();
		if (dir != null) {
			dir = dir.trim();
			if (dir.length() == 0) {
				return null;
			}
			lastPath = dir;
		}
		return dir;
	}

}
