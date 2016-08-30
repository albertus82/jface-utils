package it.albertus.jface.preference.page;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.widgets.Composite;

public class FieldEditorWrapper {

	private final FieldEditor fieldEditor;
	private final Composite parent;

	public FieldEditorWrapper(final FieldEditor fieldEditor, final Composite parent) {
		this.fieldEditor = fieldEditor;
		this.parent = parent;
	}

	public final FieldEditor getFieldEditor() {
		return fieldEditor;
	}

	public final Composite getParent() {
		return parent;
	}

}
