package it.albertus.jface.preference.validation;

import it.albertus.jface.validation.TextValidator;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.widgets.Text;

public class StringFieldEditorValidator extends TextValidator {

	protected final FieldEditor fieldEditor;

	public StringFieldEditorValidator(final Text text, final FieldEditor fieldEditor) {
		super(text);
		this.fieldEditor = fieldEditor;
	}

	@Override
	public boolean isValid() {
		return fieldEditor.isValid();
	}

}
